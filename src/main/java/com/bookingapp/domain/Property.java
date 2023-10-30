package com.bookingapp.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Property {

    private Long id;
    private String name;
    private Double pricePerDay;
    private PropertyStatus status;
    private Date createdAt;
    private Date lastUpdatedAt;
    private User propertyOwner;

}
