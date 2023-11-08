package com.bookingapp.controller;

import com.bookingapp.domain.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class BookingUpdateRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    private Long userId;

}
