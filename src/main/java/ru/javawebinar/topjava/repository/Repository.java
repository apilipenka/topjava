package ru.javawebinar.topjava.repository;

import java.util.List;

public interface Repository<T, I> {
    T create(T t);

    T get(I id);

    void delete(I id);

    T update(T t);

    List<T> getAll();
}
