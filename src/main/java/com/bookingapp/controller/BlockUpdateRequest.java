package com.bookingapp.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BlockUpdateRequest {

    @NotNull
    private Long userId;

    private Date startDate;

    private Date endDate;
}
