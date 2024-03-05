package ru.javawebinar.topjava.repository.datajpa;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

public interface DataJpaMealRepositoryInt extends MealRepository {

    Meal getWithUser(int id, int userId);

}