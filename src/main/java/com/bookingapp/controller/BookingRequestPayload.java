package com.bookingapp.controller;

import com.bookingapp.domain.BookingType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class BookingRequestPayload {

    @NotNull
    private Long propertyId;

    @NotNull
    private Long userId;

    @NotNull
    private BookingType bookingType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
