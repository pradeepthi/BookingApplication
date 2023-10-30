package com.bookingapp.repository.entity;

import com.bookingapp.domain.UserRole;
import com.bookingapp.domain.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "users")
@Table(indexes = {
    @Index(name = "idx_user_status", columnList = "status")
})
@Data
@SuperBuilder
@NoArgsConstructor
public class UserEntity extends BaseEntity{

    @NotEmpty
    @Column
    private String name;

    @NotEmpty
    @Email
    @Column
    private String email;

    @Column
    private UserStatus status;

    @Column
    private UserRole userRole;
}
