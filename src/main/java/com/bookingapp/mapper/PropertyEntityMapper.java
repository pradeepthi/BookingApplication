package com.bookingapp.mapper;

import com.bookingapp.domain.Property;
import com.bookingapp.domain.User;
import com.bookingapp.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bookingapp.repository.entity.PropertyEntity;
import com.bookingapp.util.IEntityMapper;

@Component
public class PropertyEntityMapper implements IEntityMapper<Property, PropertyEntity> {

    @Autowired
    private IEntityMapper<User, UserEntity> userEntityMapper;

    @Override
    public PropertyEntity mapToEntity(Property property) {
        return PropertyEntity.builder()
                .id(property.getId())
                .name(property.getName())
                .pricePerDay(property.getPricePerDay())
                .createdAt(property.getCreatedAt())
                .lastUpdatedAt(property.getLastUpdatedAt())
                .build();
    }

    @Override
    public Property mapToDomain(PropertyEntity propertyEntity) {
        return Property.builder()
                .id(propertyEntity.getId())
                .name(propertyEntity.getName())
                .pricePerDay(propertyEntity.getPricePerDay())
                .createdAt(propertyEntity.getCreatedAt())
                .lastUpdatedAt(propertyEntity.getLastUpdatedAt())
                .build();
    }
}
