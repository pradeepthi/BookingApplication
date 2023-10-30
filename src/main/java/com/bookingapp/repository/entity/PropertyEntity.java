package com.bookingapp.repository.entity;

import com.bookingapp.domain.PropertyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.catalina.User;


@Entity(name = "properties")
@Table(indexes = {
        @Index(name="idx_property_status", columnList = "status")
})
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

    @Column
    private PropertyStatus status;

    @ManyToOne
    private UserEntity propertyOwner;

}
