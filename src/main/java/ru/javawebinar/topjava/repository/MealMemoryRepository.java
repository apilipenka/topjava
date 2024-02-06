package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealMemoryRepository implements Repository<Meal, Integer> {
    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static volatile MealMemoryRepository instance;
    private static Integer idCounter = Integer.valueOf(0);

    public MealMemoryRepository() {
        create(
                new Meal(null,
                        LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак",
                        500));

        create(
                new Meal(null,
                        LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                        "Обед",
                        1000));
        create(
                new Meal(null,
                        LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                        "Ужин",
                        500));
        create(
                new Meal(null,
                        LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                        "Еда на граничное значение",
                        100));
        create(
                new Meal(null,
                        LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                        "Завтрак",
                        1000));
        create(
                new Meal(null,
                        LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                        "Обед",
                        500));
        create(
                new Meal(null,
                        LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                        "Ужин",
                        410));
    }

    public static MealMemoryRepository getInstance() {
        MealMemoryRepository localInstance = instance;
        if (localInstance == null) {
            synchronized (MealMemoryRepository.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MealMemoryRepository();
                }
            }
        }
        return localInstance;
    }

    public static synchronized Integer createID() {
        idCounter = idCounter + 1;
        return idCounter;
    }

    @Override
    public Meal create(Meal meal) {
        Integer mealId = createID();

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
        if (meals.containsKey(meal.id)) {
            meals.replace(meal.id, meal);
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
