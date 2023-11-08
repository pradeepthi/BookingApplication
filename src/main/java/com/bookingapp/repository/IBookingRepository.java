package com.bookingapp.repository;

import com.bookingapp.domain.BookingStatus;
import com.bookingapp.repository.entity.BookingEntity;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IBookingRepository extends JpaRepository<BookingEntity, Long> {

    /**
     * This method returns the properties which are booked or blocked and overlapped
     * in the given date range.
     * @param propertyId
     * @param startDate
     * @param endDate
     * @return
     */
    @Query("SELECT b FROM bookings b WHERE b.property.id = :propertyId  " +
            "AND b.status = 0 AND (:startDate <= b.endDate) AND (:endDate >= b.startDate)")
    List<BookingEntity> findByOverlappingBookings(@Param("propertyId") Long propertyId,
                                                             @Param("startDate") LocalDate startDate,
                                                             @Param("endDate") LocalDate endDate);


    @Query("SELECT b from bookings b WHERE b.property.id = :propertyId AND b.bookingType = 0 " +
            "AND b.status = 0 " +
            "AND (:startDate <= b.endDate) AND (:endDate >= b.startDate)")
    List<BookingEntity> findReservationsOverlappedInDateRange(@Param("propertyId") Long propertyId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM bookings b WHERE b.property.id = :propertyId  " +
            "AND b.user.id != :userId " +
            "AND b.status = 0 AND (:startDate <= b.endDate) AND (:endDate >= b.startDate)")
    List<BookingEntity> findByOverlappingBookingsByOtherUsers(@Param("propertyId") Long propertyId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate, @Param("userId") Long userId);


}
