package ru.javawebinar.topjava.repository.datajpa;

import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

public interface DataJpaUserRepositoryInt extends UserRepository {
    User getWithMeal(int id);
}