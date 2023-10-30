package com.bookingapp.service;

import com.bookingapp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.bookingapp.repository.IUserRepository;
import com.bookingapp.repository.entity.UserEntity;
import com.bookingapp.util.IEntityMapper;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IEntityMapper<User, UserEntity> userEntityMapper;

    public User findById(Long id){

        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isPresent()){
            return userEntityMapper.mapToDomain(user.get());
        }

        return null;
    }


}
