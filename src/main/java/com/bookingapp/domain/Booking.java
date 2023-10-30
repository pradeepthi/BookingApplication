package com.bookingapp.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Booking {

    private Long id;
    private Property property;
    private User user;
    private Date startDate;
    private Date endDate;
    private BookingStatus status;
    private Date createdAt;
    private Date lastUpdatedAt;
}
