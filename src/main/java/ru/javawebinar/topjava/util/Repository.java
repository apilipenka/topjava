package ru.javawebinar.topjava.util;

import java.util.List;

public interface Repository<T, I> {
    I create(T t);

    T get(I id);

    void delete(I id);

    void update(T t);

    List<T> find(T t);

    List<T> getAll();
}
