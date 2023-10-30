package com.bookingapp.repository;

import com.bookingapp.repository.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPropertyRepository extends JpaRepository<PropertyEntity, Long> {
}
