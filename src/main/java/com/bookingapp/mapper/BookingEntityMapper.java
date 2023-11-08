package com.bookingapp.mapper;

import com.bookingapp.domain.Booking;
import com.bookingapp.domain.Property;
import com.bookingapp.domain.User;
import com.bookingapp.repository.entity.BookingEntity;
import com.bookingapp.repository.entity.PropertyEntity;
import com.bookingapp.repository.entity.UserEntity;
import com.bookingapp.util.IEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingEntityMapper implements IEntityMapper<Booking, BookingEntity> {

    @Autowired
    private IEntityMapper<User, UserEntity> userEntityMapper;

    @Autowired
    private IEntityMapper<Property, PropertyEntity> propertyEntityMapper;

    @Override
    public BookingEntity mapToEntity(Booking booking) {
        return BookingEntity.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .bookingType(booking.getBookingType())
                .status(booking.getStatus())
                .user(userEntityMapper.mapToEntity(booking.getUser()))
                .property(propertyEntityMapper.mapToEntity(booking.getProperty()))
                .lastUpdatedAt(booking.getLastUpdatedAt())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    @Override
    public Booking mapToDomain(BookingEntity bookingEntity) {
        return Booking.builder()
                .id(bookingEntity.getId())
                .startDate(bookingEntity.getStartDate())
                .endDate(bookingEntity.getEndDate())
                .bookingType(bookingEntity.getBookingType())
                .status(bookingEntity.getStatus())
                .user(userEntityMapper.mapToDomain(bookingEntity.getUser()))
                .property(propertyEntityMapper.mapToDomain(bookingEntity.getProperty()))
                .lastUpdatedAt(bookingEntity.getLastUpdatedAt())
                .createdAt(bookingEntity.getCreatedAt())
                .build();
    }
}
