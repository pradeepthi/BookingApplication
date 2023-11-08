package com.bookingapp.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity(name = "properties")
@Data
@SuperBuilder
@NoArgsConstructor
public class PropertyEntity extends BaseEntity {

    @NotEmpty
    @Column
    private String name;

    @NotNull
    @Column
    private Double pricePerDay;

    @ManyToOne
    private UserEntity propertyOwner;

}
