package com.bookingapp.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BookingRequestPayload {

    @NotNull
    private Long propertyId;

    @NotNull
    private Long userId;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;
}
