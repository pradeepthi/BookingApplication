package com.bookingapp.controller;

import com.bookingapp.domain.Booking;
import com.bookingapp.domain.BookingStatus;
import com.bookingapp.domain.Property;
import com.bookingapp.domain.User;
import com.bookingapp.exception.BookingApplicationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookingapp.service.BookingService;
import com.bookingapp.service.PropertyService;
import com.bookingapp.service.UserService;
import com.bookingapp.util.ErrorCode;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    /**
     * Create a booking
     * @param bookingRequestPayload
     * @return
     */
    @PostMapping
    public ResponseEntity<?> bookProperty(@RequestBody @NotNull @Valid BookingRequestPayload bookingRequestPayload){

        //validate booking request payload
        isValidBookingRequestPayload(bookingRequestPayload);

        //map request payload to domain
        Booking booking = buildBooking(bookingRequestPayload);

        Booking createdBooking = bookingService.createBooking(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);

    }

    private Booking buildBooking(final BookingRequestPayload bookingRequestPayload){
        return Booking.builder()
                .property(Property.builder()
                        .id(bookingRequestPayload.getPropertyId())
                        .build())
                .startDate(bookingRequestPayload.getStartDate())
                .endDate(bookingRequestPayload.getEndDate())
                .user(User.builder()
                        .id(bookingRequestPayload.getUserId())
                        .build())
                .createdAt(new Date(System.currentTimeMillis()))
                .lastUpdatedAt(new Date(System.currentTimeMillis()))
                .status(BookingStatus.RESERVED)
                .build();
    }

    private void isValidBookingRequestPayload(BookingRequestPayload bookingRequestPayload){

        //validate userId and status
        bookingService.isValidBookingUser(bookingRequestPayload.getUserId());

        //validate propertyId
        bookingService.isValidBookingProperty(bookingRequestPayload.getPropertyId());

    }

    /**
     * This API can update only startDate, endDate and status of booking.
     * @param updateRequest
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody @Valid @NotNull BookingUpdateRequest updateRequest) {

        isValidBookingUpdateRequest(id, updateRequest);
        Booking updatedBooking = bookingService.updateBooking(id, updateRequest);

        return ResponseEntity.ok(updatedBooking);
    }

    private void isValidBookingUpdateRequest(Long bookingId, BookingUpdateRequest updateRequest) {

        //validate userId and status
        bookingService.isValidBookingUser(updateRequest.getUserId());

        //check if property exists and if booking is done by the user
        Booking existingBooking = bookingService.findById(bookingId);
        if(existingBooking.getUser().getId() != updateRequest.getUserId()) {
            throw new BookingApplicationException(ErrorCode.INVALID_REQUEST);
        }

        //validate startDate and end Date
        if((updateRequest.getStartDate() != null) && (updateRequest.getEndDate() != null)
                && (updateRequest.getStartDate().compareTo(updateRequest.getEndDate()) > 0)) {
            throw new BookingApplicationException(ErrorCode.INVALID_REQUEST);
        }

    }

    /**
     * This API will get booking details by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {

        Booking existingBooking = bookingService.findById(id);
        if(existingBooking == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(existingBooking);
        }
    }

    /**
     * This API deletes booking by Id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingById(@PathVariable Long id) {
        Booking existingBooking = bookingService.deleteById(id);
        return ResponseEntity.ok(existingBooking);
    }
}
