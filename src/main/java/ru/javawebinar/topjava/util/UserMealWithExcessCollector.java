package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class UserMealWithExcessCollector
        implements Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>> {

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;
    private final Map<LocalDate, Integer> totalCaloriesByDateMap = new HashMap<>();
    private final Map<LocalDate, AtomicBoolean> isExcessByDateMap = new HashMap<>();

    public UserMealWithExcessCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    @Override
    public Supplier<List<UserMealWithExcess>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
        return (list, userMeal) -> {
            MealUtil.fillMapsWithStatistics(caloriesPerDay, userMeal, totalCaloriesByDateMap, isExcessByDateMap);

            if (TimeUtil.isBetweenHalfOpen(
                    userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(
                        new UserMealWithExcess(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                isExcessByDateMap.get(userMeal.getDateTime().toLocalDate())));
            }
        };
    }

    @Override
    public BinaryOperator<List<UserMealWithExcess>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
        return list -> list;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}
