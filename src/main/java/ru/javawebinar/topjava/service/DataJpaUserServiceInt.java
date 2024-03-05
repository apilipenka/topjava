package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.User;

public interface DataJpaUserServiceInt extends UserServiceInt {
    User getWithMeal(int id);
}