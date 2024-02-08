package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MemoryMealRepository implements Repository<Meal, Integer> {
    private static final Logger log = getLogger(MemoryMealRepository.class);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public MemoryMealRepository() {
        Arrays.asList(
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                                "Завтрак", 500),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                                "Обед", 1000),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                                "Ужин", 500),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                                "Еда на граничное значение", 100),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                                "Завтрак", 1000),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                                "Обед", 500),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                                "Ужин", 410))
                .forEach(this::create);
    }

    public synchronized Integer createId() {
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
        return meals.computeIfPresent(meal.getId(), (i, oldmeal) -> meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
