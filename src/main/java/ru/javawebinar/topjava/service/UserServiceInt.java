package ru.javawebinar.topjava.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserServiceInt {
    @CacheEvict(value = "users", allEntries = true)
    User create(User user);

    @CacheEvict(value = "users", allEntries = true)
    void delete(int id);

    User get(int id);

    User getByEmail(String email);

    @Cacheable("users")
    List<User> getAll();

    @CacheEvict(value = "users", allEntries = true)
    void update(User user);
}