package com.bookingapp.controller;

import com.bookingapp.domain.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BookingUpdateRequest {

    private Date startDate;
    private Date endDate;
    private BookingStatus status;

    @NotNull
    private Long userId;

}
