package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MealMemoryRepository implements Repository<Meal, Integer> {

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public Integer create(Meal meal) {
        Integer id = 1;
        synchronized (meals) {
            if (!meals.isEmpty()) {
                id = meals.keySet().stream().max(Integer::compare).get() + 1;
            }
            meal.setId(id);
            meals.put(id, meal);
        }
        return id;
    }

    @Override
    public Meal get(Integer id) {
        synchronized (meals) {
            return meals.get(id);
        }
    }

    @Override
    public void delete(Integer id) {
        synchronized (meals) {
            meals.remove(id);
        }
    }

    @Override
    public void update(Meal meal) {
        synchronized (meals) {
            meals.remove(meal.id);
            meals.put(meal.id, meal);
        }
    }

    @Override
    public List<Meal> find(Meal meal) {
        synchronized (meals) {
            return meals.values().stream().filter(mealItem -> (meal.getId() == null || meal.getId().equals(mealItem.getId())) &&
                    (meal.getDateTime() == null || meal.getDateTime().equals(mealItem.getDateTime())) &&
                    (meal.getDescription() == null || meal.getDescription().equals(mealItem.getDescription())) &&
                    (meal.getCalories() == null || meal.getCalories().equals(mealItem.getCalories()))).collect(Collectors.toList());
        }
    }

    @Override
    public List<Meal> getAll() {
        synchronized (meals) {
            return new ArrayList<>(meals.values());
        }
    }
}
