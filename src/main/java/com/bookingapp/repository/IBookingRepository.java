package com.bookingapp.repository;

import com.bookingapp.domain.BookingStatus;
import com.bookingapp.repository.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookingRepository extends JpaRepository<BookingEntity, Long> {

    public Optional<BookingEntity> findByPropertyIdAndStatus(Long propertyId, BookingStatus status);
}
