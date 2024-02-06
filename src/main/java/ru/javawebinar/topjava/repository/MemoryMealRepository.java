package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealRepository implements Repository<Meal, Integer> {
    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public MemoryMealRepository() {
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Завтрак", 500));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед", 1000));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                "Ужин", 500));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                "Еда на граничное значение", 100));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                "Завтрак", 1000));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                "Обед", 500));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                "Ужин", 410));
    }

    public static synchronized Integer createId() {
        return idCounter.incrementAndGet();
    }

    @Override
    public Meal create(Meal meal) {
        Integer mealId = createId();
        meal.setId(mealId);
        meals.put(mealId, meal);
        return meal;
    }

    @Override
    public Meal get(Integer id) {
        return meals.get(id);
    }

    @Override
    public void delete(Integer id) {
        meals.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        return meals.replace(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
