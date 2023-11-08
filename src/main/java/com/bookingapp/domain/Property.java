package com.bookingapp.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class Property {

    private Long id;
    private String name;
    private Double pricePerDay;
    private LocalDate createdAt;
    private LocalDate lastUpdatedAt;
    private User propertyOwner;

}
