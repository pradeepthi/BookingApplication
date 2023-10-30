package com.bookingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

@Configuration
@ComponentScan(basePackages = { "com.bookingapp"})
public class AppConfig {

    @Bean
    public LockRegistry lockRegistry() {
        return new DefaultLockRegistry();
    }

}
