package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> meals = repository.computeIfAbsent(userId, (id) -> new ConcurrentHashMap<>());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        Meal mealFromRepository = get(meal.getId(), userId);
        if (isBelongToUser(mealFromRepository, userId)) {
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        for (Map<Integer, Meal> meals : repository.values()) {
            Meal mealFromRepository = meals.get(id);
            if (isBelongToUser(mealFromRepository, userId)) {
                mealFromRepository = meals.remove(id);
                return mealFromRepository != null;
            }
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.values().stream()
                .map(meals -> meals.get(id))
                .filter(mealFromRepository -> isBelongToUser(mealFromRepository, userId))
                .findFirst().orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFilterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getAllFilterByPredicate(userId,
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate));
    }

    private boolean isBelongToUser(Meal meal, int userId) {
        return meal != null && meal.getUserId() == userId;
    }


    private List<Meal> getAllFilterByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> mealsRepository = repository.get(userId);
        List<Meal> meals = new ArrayList<>();
        if (mealsRepository == null || mealsRepository.isEmpty()) {
            return meals;
        }
        return mealsRepository.values()
                .stream()
                .filter(filter)
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }
}

