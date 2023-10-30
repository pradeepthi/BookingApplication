package com.bookingapp.util;

public interface IEntityMapper<D, E> {

    public E mapToEntity(D d);
    public D mapToDomain(E e);

}
