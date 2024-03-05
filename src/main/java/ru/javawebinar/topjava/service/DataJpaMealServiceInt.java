package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

public interface DataJpaMealServiceInt extends MealServiceInt {
    Meal getWithUser(int id, int userId);
}