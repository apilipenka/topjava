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
import java.util.stream.Collectors;

public class UserMealWithExcessCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {
    LocalTime startTime;
    LocalTime endTime;
    int caloriesPerDay;
    Map<LocalDate, Integer> totalCaloriesByDateMap = new HashMap<>();

    public UserMealWithExcessCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    @Override
    public Supplier<List<UserMeal>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<UserMeal>, UserMeal> accumulator() {

        return (list, userMeal) -> {
            totalCaloriesByDateMap.put(userMeal.getDateTime().toLocalDate(),
                    totalCaloriesByDateMap.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) + userMeal.getCalories());
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                list.add(userMeal);
        };
    }

    @Override
    public BinaryOperator<List<UserMeal>> combiner() {
        return (list1, list2) -> {

            list2.forEach(userMeal -> totalCaloriesByDateMap.put(userMeal.getDateTime().toLocalDate(),
                    totalCaloriesByDateMap.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) + userMeal.getCalories()));

            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
        return list -> list.stream()
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        new AtomicBoolean(totalCaloriesByDateMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}