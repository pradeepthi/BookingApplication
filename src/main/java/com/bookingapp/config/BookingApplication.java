package com.bookingapp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.bookingapp"})
@EntityScan(basePackages = { "com.bookingapp"})
@EnableJpaRepositories(basePackages = {"com.bookingapp"})
public class BookingApplication {

    public static void main(String args[]) {
        SpringApplication.run(BookingApplication.class, args);
    }
}
