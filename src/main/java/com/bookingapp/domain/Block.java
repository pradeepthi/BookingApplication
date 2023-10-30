package com.bookingapp.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Block {

    private Long id;
    private Property property;
    private Date startDate;
    private Date endDate;
    private User user;
    private Date createdAt;
    private Date lastUpdatedAt;

}