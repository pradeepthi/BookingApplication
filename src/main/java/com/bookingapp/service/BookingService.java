package com.bookingapp.service;

import com.bookingapp.controller.BookingUpdateRequest;
import com.bookingapp.domain.*;
import com.bookingapp.exception.BookingApplicationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;
import com.bookingapp.repository.IBookingRepository;
import com.bookingapp.repository.entity.BookingEntity;
import com.bookingapp.util.ErrorCode;
import com.bookingapp.util.IEntityMapper;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

@Service
public class BookingService {

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private LockRegistry lockRegistry;

    @Autowired
    private IEntityMapper<Booking, BookingEntity> bookingEntityMapper;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Transactional
    public Booking createBooking(Booking booking){

        Booking result = null;

        //create key on propertyid
        Lock lock = lockRegistry.obtain("property_"+booking.getProperty().getId());
        try {
            if(lock.tryLock()) {
                //double validation
                isValidBookingUser(booking.getUser().getId());
                isValidBookingProperty(booking.getProperty().getId());
                BookingEntity newBooking = bookingEntityMapper.mapToEntity(booking);
                result = bookingEntityMapper.mapToDomain(bookingRepository.save(newBooking));
            }

        } finally {
            lock.unlock();
        }

        return result;

    }

    public void isValidBookingProperty(Long propertyId) {

        Property existingProperty = propertyService.findById(propertyId);
        if(null == existingProperty){
            throw new BookingApplicationException(ErrorCode.INVALID_PROPERTY_ID);
        }

        if(existingProperty.getStatus() != PropertyStatus.OPEN) {
            throw new BookingApplicationException(ErrorCode.PROPERTY_UNAVAILABLE);
        }
    }

    public void isValidBookingUser(Long userId) {

        User user = userService.findById(userId);
        if(null == user || (user.getStatus() != UserStatus.ACTIVE) || (user.getRole() != UserRole.GUEST)) {
            throw new BookingApplicationException(ErrorCode.INVALID_USER_ID);
        }

    }

    public boolean isPropertyBooked(Long propertyId) {

        Optional<BookingEntity> bookedEntity = bookingRepository.findByPropertyIdAndStatus(propertyId, BookingStatus.RESERVED);
        if(bookedEntity.isPresent()) {
            return true;
        }

        return false;
    }

    public Booking findById(Long bookingId) {
        Optional<BookingEntity> existingBooking = bookingRepository.findById(bookingId);
        if(existingBooking.isPresent()){
            return bookingEntityMapper.mapToDomain(existingBooking.get());
        }

        return null;
    }

    /**
     * Update startDate, endDate and status of a booking. The user that created the booking can only update the booking details
     * @param bookingId
     * @param updateRequest
     * @return
     */
    public Booking updateBooking(Long bookingId, BookingUpdateRequest updateRequest) {

        Lock lock = lockRegistry.obtain("booking_"+bookingId);
        try {

            if(lock.tryLock()) {
                //get booking details from database
                BookingEntity existingBooking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new BookingApplicationException(ErrorCode.INVALID_REQUEST));

                //update it with the requested data
                if(null != updateRequest.getStartDate()) {
                    existingBooking.setStartDate(updateRequest.getStartDate());
                }

                if(null != updateRequest.getEndDate()) {
                    existingBooking.setEndDate(updateRequest.getEndDate());
                }

                if(null != updateRequest.getStatus()) {
                    existingBooking.setStatus(updateRequest.getStatus());
                }

                existingBooking.setLastUpdatedAt(new Date(System.currentTimeMillis()));
                return bookingEntityMapper.mapToDomain(bookingRepository.save(existingBooking));
            }

        } finally {
            lock.unlock();
        }

        return null;

    }

    /**
     * This method deletes booking by id
     * @param bookingId
     * @return
     */
    public Booking deleteById(Long bookingId) {

        Lock lock = lockRegistry.obtain("booking_"+bookingId);
        try {

            if(lock.tryLock()) {
                //get booking details from database
                BookingEntity existingBooking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new BookingApplicationException(ErrorCode.INVALID_REQUEST));

                bookingRepository.deleteById(bookingId);

                return bookingEntityMapper.mapToDomain(existingBooking);
            }

        } finally {
            lock.unlock();
        }

        return null;

    }

}
