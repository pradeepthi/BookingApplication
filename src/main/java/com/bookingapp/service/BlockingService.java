package com.bookingapp.service;

import com.bookingapp.controller.BlockUpdateRequest;
import com.bookingapp.domain.*;
import com.bookingapp.exception.BookingApplicationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;
import com.bookingapp.repository.IBlockRepository;
import com.bookingapp.repository.entity.BlockEntity;
import com.bookingapp.util.ErrorCode;
import com.bookingapp.util.IEntityMapper;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

@Service
public class BlockingService {

    @Autowired
    private IBlockRepository blockRepository;

    @Autowired
    private LockRegistry lockRegistry;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private IEntityMapper<Block, BlockEntity> blockEntityMapper;

    @Autowired
    private BookingService bookingService;

    @Transactional
    public Block blockProperty(Block block){

        Block createdBlock = null;

        Lock lock = lockRegistry.obtain("property_"+block.getProperty().getId());
        try {
            if(lock.tryLock()) {
                //validate property
                isValidBlockProperty(block.getProperty().getId());

                //validate User
                isValidBlockUser(block.getUser().getId());
                BlockEntity blockToCreate = blockEntityMapper.mapToEntity(block);
                createdBlock = blockEntityMapper.mapToDomain(blockRepository.save(blockToCreate));

                //update property status to blocked
                updatePropertyStatusToBlocked(block.getProperty().getId());
            }

        } finally {
            lock.unlock();
        }

        return createdBlock;

    }

    public void isValidBlockProperty(Long propertyId) {

        Property existingProperty = propertyService.findById(propertyId);
        if(null == existingProperty){
            throw new BookingApplicationException(ErrorCode.INVALID_PROPERTY_ID);
        }

        //check property status and booking status
        if((existingProperty.getStatus() == PropertyStatus.BLOCKED)
                || bookingService.isPropertyBooked(propertyId)) {
            throw new BookingApplicationException(ErrorCode.PROPERTY_UNAVAILABLE);
        }

    }

    public void isValidBlockUser(Long userId) {

        User user = userService.findById(userId);
        if(null == user || (user.getStatus() != UserStatus.ACTIVE) || (user.getRole() == UserRole.GUEST)) {
            throw new BookingApplicationException(ErrorCode.INVALID_USER_ID);
        }

    }

    public Block findById(Long blockId) {
        Optional<BlockEntity> existingBlock = blockRepository.findById(blockId);
        if(existingBlock.isPresent()){
            return blockEntityMapper.mapToDomain(existingBlock.get());
        }

        return null;
    }

    /**
     * This method updates startDate and endDate of  a blocked property
     * @param blockId
     * @param updateRequest
     * @return
     */
    public Block updateBlockDetails(Long blockId, BlockUpdateRequest updateRequest) {

        Lock lock = lockRegistry.obtain("block_"+blockId);
        try {

            if(lock.tryLock()) {
                //get block details from database
                BlockEntity existingBlock = blockRepository.findById(blockId)
                        .orElseThrow(() -> new BookingApplicationException(ErrorCode.INVALID_REQUEST));

                //update it with the requested data
                if(null != updateRequest.getStartDate()) {
                    existingBlock.setStartDate(updateRequest.getStartDate());
                }

                if(null != updateRequest.getEndDate()) {
                    existingBlock.setEndDate(updateRequest.getEndDate());
                }

                existingBlock.setLastUpdatedAt(new Date(System.currentTimeMillis()));
                return blockEntityMapper.mapToDomain(blockRepository.save(existingBlock));
            }

        } finally {
            lock.unlock();
        }

        return null;

    }

    /**
     * This method deletes block by Id
     * @param blockId
     * @return
     */
    public Block deleteById(Long blockId) {

        Lock lock = lockRegistry.obtain("block_"+blockId);
        try {

            if(lock.tryLock()) {
                //get block details from database
                BlockEntity existingBlock = blockRepository.findById(blockId)
                        .orElseThrow(() -> new BookingApplicationException(ErrorCode.INVALID_REQUEST));

                blockRepository.deleteById(blockId);

                return blockEntityMapper.mapToDomain(existingBlock);
            }

        } finally {
            lock.unlock();
        }

        return null;

    }

    public Property updatePropertyStatusToBlocked(Long propertyId){

        Property property = propertyService.findById(propertyId);
        property.setStatus(PropertyStatus.BLOCKED);
        return propertyService.save(property);

    }

}
