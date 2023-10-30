package com.bookingapp.service;

import com.bookingapp.domain.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookingapp.repository.IPropertyRepository;
import com.bookingapp.repository.entity.PropertyEntity;
import com.bookingapp.util.IEntityMapper;

import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private IPropertyRepository propertyRepository;

    @Autowired
    private IEntityMapper<Property, PropertyEntity> propertyEntityMapper;

    public Property findById(Long propertyId){
        Optional<PropertyEntity> existingProperty = propertyRepository.findById(propertyId);
        if(existingProperty.isPresent()){
            return propertyEntityMapper.mapToDomain(existingProperty.get());
        }

        return null;
    }


    public Property save(Property property) {
        return propertyEntityMapper.mapToDomain(propertyRepository.save(propertyEntityMapper.mapToEntity(property)));
    }

}
