package com.bookingapp.mapper;

import com.bookingapp.domain.Block;
import com.bookingapp.domain.Property;
import com.bookingapp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bookingapp.repository.entity.BlockEntity;
import com.bookingapp.repository.entity.PropertyEntity;
import com.bookingapp.repository.entity.UserEntity;
import com.bookingapp.util.IEntityMapper;

@Component
public class BlockEntityMapper implements IEntityMapper<Block, BlockEntity> {

    @Autowired
    private IEntityMapper<User, UserEntity> userEntityMapper;

    @Autowired
    private IEntityMapper<Property, PropertyEntity> propertyEntityMapper;

    @Override
    public BlockEntity mapToEntity(Block block) {
        return BlockEntity.builder()
                .id(block.getId())
                .startDate(block.getStartDate())
                .endDate(block.getEndDate())
                .user(userEntityMapper.mapToEntity(block.getUser()))
                .property(propertyEntityMapper.mapToEntity(block.getProperty()))
                .createdAt(block.getCreatedAt())
                .lastUpdatedAt(block.getLastUpdatedAt())
                .build();
    }

    @Override
    public Block mapToDomain(BlockEntity blockEntity) {
        return Block.builder()
                .id(blockEntity.getId())
                .startDate(blockEntity.getStartDate())
                .endDate(blockEntity.getEndDate())
                .property(propertyEntityMapper.mapToDomain(blockEntity.getProperty()))
                .user(userEntityMapper.mapToDomain(blockEntity.getUser()))
                .createdAt(blockEntity.getCreatedAt())
                .lastUpdatedAt(blockEntity.getLastUpdatedAt())
                .build();
    }
}
