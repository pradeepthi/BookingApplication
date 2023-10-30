package com.bookingapp.mapper;

import com.bookingapp.domain.User;
import org.springframework.stereotype.Component;
import com.bookingapp.repository.entity.UserEntity;
import com.bookingapp.util.IEntityMapper;

@Component
public class UserEntityMapper implements IEntityMapper<User, UserEntity> {

    @Override
    public UserEntity mapToEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .lastUpdatedAt(user.getLastUpdatedAt())
                .userRole(user.getRole())
                .build();
    }

    @Override
    public User mapToDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .status(userEntity.getStatus())
                .createdAt(userEntity.getCreatedAt())
                .lastUpdatedAt(userEntity.getLastUpdatedAt())
                .role(userEntity.getUserRole())
                .build();
    }
}
