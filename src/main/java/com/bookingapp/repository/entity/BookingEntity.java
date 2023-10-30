package com.bookingapp.repository.entity;

import com.bookingapp.domain.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity(name = "bookings")
@SuperBuilder
@Data
@Table(indexes = {
        @Index(name = "idx_booking_status", columnList = "status")
})
@NoArgsConstructor
public class BookingEntity extends BaseEntity {

    @NotNull
    @Column
    private Date startDate;

    @NotNull
    @Column
    private Date endDate;

    @Column
    private BookingStatus status;

    @ManyToOne
    private UserEntity guest;

    @ManyToOne
    private PropertyEntity property;

}
