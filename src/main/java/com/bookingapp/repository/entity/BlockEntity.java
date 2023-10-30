package com.bookingapp.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity(name = "blocks")
@SuperBuilder
@Data
@NoArgsConstructor
public class BlockEntity extends BaseEntity{

    @ManyToOne
    private PropertyEntity property;

    @NotNull
    @Column
    private Date startDate;

    @NotNull
    @Column
    private Date endDate;

    @ManyToOne
    private UserEntity user;
}
