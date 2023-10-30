package com.bookingapp.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BlockRequestPayload {

    @NotNull
    private Long userId;

    @NotNull
    private Long propertyId;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;
}
