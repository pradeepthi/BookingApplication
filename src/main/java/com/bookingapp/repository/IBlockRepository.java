package com.bookingapp.repository;

import com.bookingapp.repository.entity.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlockRepository extends JpaRepository<BlockEntity, Long> {
}
