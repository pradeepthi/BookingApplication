package com.bookingapp.repository.entity;

import com.bookingapp.domain.BookingStatus;
import com.bookingapp.domain.BookingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "bookings")
@SuperBuilder
@Data
@Table(indexes = {
        @Index(name = "idx_booking_status", columnList = "status"),
        @Index(name = "idx_booking_dates", columnList = "startDate, endDate")
})
@NoArgsConstructor
public class BookingEntity extends BaseEntity {

    @NotNull
    @Column
    private LocalDate startDate;

    @NotNull
    @Column
    private LocalDate endDate;

    @Column
    private BookingType bookingType;

    @Column
    private BookingStatus status;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private PropertyEntity property;

}
