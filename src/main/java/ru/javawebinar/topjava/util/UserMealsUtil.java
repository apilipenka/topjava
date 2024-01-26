package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByCyclesOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByStreamsOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            dateMap.put(userMeal.getDateTime().toLocalDate(), dateMap.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) + userMeal.getCalories());
        }

        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), new AtomicBoolean(dateMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)));
            }
        }

        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateMap = new HashMap<>();
        meals.forEach(userMeal -> dateMap.put(userMeal.getDateTime().toLocalDate(), dateMap.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) + userMeal.getCalories()));

        return meals.stream().
                filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)).
                map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), new AtomicBoolean(dateMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))).
                collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateMap = new HashMap<>();
        Map<LocalDate, AtomicBoolean> dateMapAtomicBoolean = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();

        for (UserMeal userMeal : meals) {
            fillMaps(caloriesPerDay, dateMap, dateMapAtomicBoolean, userMeal);

            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), dateMapAtomicBoolean.get(userMeal.getDateTime().toLocalDate())));
            }
        }

        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateMap = new HashMap<>();
        Map<LocalDate, AtomicBoolean> dateMapAtomicBoolean = new HashMap<>();

        return
                meals.stream().
                        peek(userMeal -> fillMaps(caloriesPerDay, dateMap, dateMapAtomicBoolean, userMeal)).
                        filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)).
                        map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), dateMapAtomicBoolean.get(userMeal.getDateTime().toLocalDate()))).
                        collect(Collectors.toList());
    }

    private static void fillMaps(int caloriesPerDay, Map<LocalDate, Integer> dateMap, Map<LocalDate, AtomicBoolean> dateMapAtomicBoolean, UserMeal userMeal) {
        LocalDate userMealLocalDate = userMeal.getDateTime().toLocalDate();

        dateMap.put(userMealLocalDate, dateMap.getOrDefault(userMealLocalDate, 0) + userMeal.getCalories());

        AtomicBoolean atomicBoolean = dateMapAtomicBoolean.getOrDefault(userMealLocalDate, new AtomicBoolean(dateMap.get(userMealLocalDate) > caloriesPerDay));
        atomicBoolean.set(dateMap.get(userMealLocalDate) > caloriesPerDay);
        dateMapAtomicBoolean.put(userMealLocalDate, atomicBoolean);
    }

}
