package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
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
        if (meal.isNew()) {
            Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, (id) -> new ConcurrentHashMap<>());
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
        } else {
            Map<Integer, Meal> mealMap = repository.get(userId);
            if (CollectionUtils.isEmpty(mealMap)) {
                return null;
            }
            return mealMap.computeIfPresent(userId, (id, oldMeal) -> meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return !CollectionUtils.isEmpty(mealMap) && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return CollectionUtils.isEmpty(mealMap) ? null : mealMap.remove(id);
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

    private List<Meal> getAllFilterByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (CollectionUtils.isEmpty(mealMap)) {
            return Collections.emptyList();
        }
        return mealMap.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

