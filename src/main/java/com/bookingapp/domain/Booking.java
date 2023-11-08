package com.bookingapp.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class Booking {

    private Long id;
    private Property property;
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingType bookingType;
    private BookingStatus status;
    private LocalDate createdAt;
    private LocalDate lastUpdatedAt;
}
