package com.bookingapp.service;

import com.bookingapp.controller.BookingRequestPayload;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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

    /**
     * This method either blocks or reserves a property.
     * @param booking
     * @return
     */

    @Transactional
    public Booking createBooking(Booking booking){

        Booking result = null;

        //create key on propertyid
        Lock lock = lockRegistry.obtain("property_"+booking.getProperty().getId());
        try {
            if(lock.tryLock()) {
                //double validation
                isPropertyAvailable(booking);
                BookingEntity newBooking = bookingEntityMapper.mapToEntity(booking);
                result = bookingEntityMapper.mapToDomain(bookingRepository.save(newBooking));
            }

        } finally {
            lock.unlock();
        }

        return result;

    }

    public void isValidBooking(Booking booking) {

        //check if valid property id
        Property existingProperty = propertyService.findById(booking.getProperty().getId());
        if(null == existingProperty){
            throw new BookingApplicationException(ErrorCode.INVALID_PROPERTY_ID);
        }

        //validate date range
        isValidDateRange(booking.getStartDate(), booking.getEndDate());

    }

    public void isValidDateRange(LocalDate startDate, LocalDate endDate) {

        if(startDate.isBefore(LocalDate.now())
                ||endDate.isBefore(startDate)) {
            throw new BookingApplicationException(ErrorCode.INVALID_DATE_RANGE);

        }

    }
    public void isPropertyAvailable(Booking booking) {

        //Depending on the booking type, check the status of the booking.
        BookingType bookingType = booking.getBookingType();
        switch (bookingType) {
            case RESERVE -> {
                //check if the property has any advanced bookings(reservations/blocks) overlapping with the booking dates.
                List<BookingEntity> overlappingBookings = bookingRepository.findByOverlappingBookings(booking.getProperty().getId(),
                        booking.getStartDate(), booking.getEndDate());
                if(!overlappingBookings.isEmpty()) {
                    throw new BookingApplicationException(ErrorCode.PROPERTY_UNAVAILABLE);
                }

                break;
            }
            case BLOCK -> {
                //check if the property is booked during this date range.
                List<BookingEntity> existingReservations = bookingRepository.findReservationsOverlappedInDateRange(booking.getProperty().getId(),
                        booking.getStartDate(), booking.getEndDate());

                if(!existingReservations.isEmpty()) {
                    throw new BookingApplicationException((ErrorCode.PROPERTY_UNAVAILABLE));
                }

                break;
            }
            default -> {
                throw new BookingApplicationException(ErrorCode.INVALID_BOOKING_TYPE);
            }
        }


    }

    public void isValidBookingUser(Long userId, BookingType bookingType) {

        User user = userService.findById(userId);
        if(null == user || (user.getStatus() != UserStatus.ACTIVE)) {
            throw new BookingApplicationException(ErrorCode.INVALID_USER_ID);
        }

        switch (bookingType) {
            case RESERVE -> {
                if(user.getRole() != UserRole.GUEST){
                    throw new BookingApplicationException(ErrorCode.NOT_ACCEPTABLE);
                }
            }
            case BLOCK -> {
                if(user.getRole() == UserRole.GUEST ){
                    throw new BookingApplicationException(ErrorCode.NOT_ACCEPTABLE);
                }
            }
            default -> {
                throw new BookingApplicationException(ErrorCode.INVALID_BOOKING_TYPE);
            }
        }

    }

    public Booking findById(Long bookingId) {
        Optional<BookingEntity> existingBooking = bookingRepository.findById(bookingId);
        if(existingBooking.isPresent()){
            return bookingEntityMapper.mapToDomain(existingBooking.get());
        }

        return null;
    }

    /**
     * This method can update only dates of a booking. Status of the booking is controlled by
     * the actions like create booking, delete booking and not done by update API
     * @param bookingId
     * @param updateRequest
     * @return
     */
    @Transactional
    public Booking updateBooking(Long bookingId, BookingUpdateRequest updateRequest) {

        //validate booking id with the user.
        Booking existingBooking = findById(bookingId);
        if(existingBooking == null) {
            throw new BookingApplicationException(ErrorCode.INVALID_REQUEST);
        }

        //To avoid concurrent property date updates for the given date range
        Lock lock = lockRegistry.obtain("property_"+existingBooking.getProperty().getId());
        try {

            if(lock.tryLock()) {

                //check if the property has any advanced bookings(reservations/blocks) overlapping with the booking dates by other users.
                List<BookingEntity> overlappingBookings = bookingRepository.findByOverlappingBookingsByOtherUsers(existingBooking.getProperty().getId(),
                        updateRequest.getStartDate(), updateRequest.getEndDate(), updateRequest.getUserId());

                if(!overlappingBookings.isEmpty()) {
                    throw new BookingApplicationException(ErrorCode.PROPERTY_UNAVAILABLE);
                }

                //create new booking object which will be used to create new booking
                Booking newBooking = existingBooking;
                newBooking.setStartDate(updateRequest.getStartDate());
                newBooking.setEndDate(updateRequest.getEndDate());
                newBooking.setLastUpdatedAt(LocalDate.now());

                //update database.
                return bookingEntityMapper.mapToDomain(bookingRepository.save(bookingEntityMapper.mapToEntity(existingBooking)));
            }

        } finally {
            lock.unlock();
        }

        return null;

    }

    /**
     * This method deletes booking by updating the status to cancelled
     * @param bookingId
     * @return
     */
    public Booking deleteById(Long bookingId) {

        return updateBookingStatus(bookingId, BookingStatus.CANCELLED);

    }

    /**
     * This method can be used to update any details of booking like status.
     * @param bookingId
     * @param bookingStatus
     * @return
     */
    private Booking updateBookingStatus(Long bookingId, BookingStatus bookingStatus) {

        Booking updatedBookingDetails = null;

        //get booking details
        Booking existingBooking = findById(bookingId);
        if(existingBooking == null) {
            throw new BookingApplicationException(ErrorCode.INVALID_REQUEST);
        }

        //status is always updated for the given bookingId and hence lock is applied on booking Id
        Lock lock = lockRegistry.obtain("booking_"+bookingId);
        try {
            if(lock.tryLock()) {

                existingBooking.setStatus(bookingStatus);
                existingBooking.setLastUpdatedAt(LocalDate.now());

                //save entity to the database and return updated booking details
                updatedBookingDetails = bookingEntityMapper.mapToDomain(bookingRepository.save(bookingEntityMapper.mapToEntity(existingBooking)));
            }

        } finally {
            lock.unlock();
        }

        return updatedBookingDetails;
    }

}
