package com.bookingapp.controller;

import com.bookingapp.domain.*;
import com.bookingapp.exception.BookingApplicationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookingapp.service.BlockingService;
import com.bookingapp.service.PropertyService;
import com.bookingapp.service.UserService;
import com.bookingapp.util.ErrorCode;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/block")
public class BlockingController {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private BlockingService blockingService;

    /**
     * This API blocks property if the property is not booked by any guest and not blocked already
     * @param blockRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<?> blockProperty(@RequestBody @NotNull @Valid BlockRequestPayload blockRequest){

        isValidBlockRequestPayload(blockRequest);
        Block blockedProperty = blockingService.blockProperty(Block.builder()
                        .property(Property.builder()
                                .id(blockRequest.getPropertyId())
                                .build())
                        .startDate(blockRequest.getStartDate())
                        .endDate(blockRequest.getEndDate())
                        .user(User.builder()
                                .id(blockRequest.getUserId())
                                .build())
                        .createdAt(new Date(System.currentTimeMillis()))
                        .lastUpdatedAt(new Date(System.currentTimeMillis()))
                .build());


        return  ResponseEntity.status(HttpStatus.CREATED).body(blockedProperty);
    }

    private void isValidBlockRequestPayload(BlockRequestPayload blockRequest) {

        //validate userId and status
        User user = userService.findById(blockRequest.getUserId());
        if(null == user || (user.getStatus() != UserStatus.ACTIVE) || (user.getRole() == UserRole.GUEST)) {
            throw new BookingApplicationException(ErrorCode.INVALID_USER_ID);
        }

        //validate propertyId
        Property property = propertyService.findById(blockRequest.getPropertyId());
        if(null == property) {
            throw new BookingApplicationException(ErrorCode.INVALID_PROPERTY_ID);
        }

        //validate property status
        if(property.getStatus() != PropertyStatus.OPEN){
            throw new BookingApplicationException(ErrorCode.PROPERTY_UNAVAILABLE);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBlockedProperty(@PathVariable Long id, @RequestBody @Valid @NotNull BlockUpdateRequest updateRequest) {

        isValidBlockUpdateRequest(id, updateRequest);
        Block updatedBlock = blockingService.updateBlockDetails(id, updateRequest);

        return ResponseEntity.ok(updatedBlock);
    }

    private void isValidBlockUpdateRequest(Long blockId, BlockUpdateRequest updateRequest) {

        //validate userId and status
        blockingService.isValidBlockUser(updateRequest.getUserId());

        //check if property exists and if block is done by the user
        Block existingBlockedProperty = blockingService.findById(blockId);
        if(existingBlockedProperty.getUser().getId() != updateRequest.getUserId()) {
            throw new BookingApplicationException(ErrorCode.INVALID_REQUEST);
        }

        //validate startDate and end Date
        if((updateRequest.getStartDate() != null) && (updateRequest.getEndDate() != null)
                && (updateRequest.getStartDate().compareTo(updateRequest.getEndDate()) < 0)) {
            throw new BookingApplicationException(ErrorCode.INVALID_REQUEST);
        }

    }

    /**
     * This API will get blocked details by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlockedPropertyById(@PathVariable Long id) {

        Block existingBlockedProperty = blockingService.findById(id);
        if(existingBlockedProperty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(existingBlockedProperty);
        }
    }

    /**
     * This API deletes blocked property by Id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingById(@PathVariable Long id) {
        Block existingBlock = blockingService.deleteById(id);
        return ResponseEntity.ok(existingBlock);
    }
}
