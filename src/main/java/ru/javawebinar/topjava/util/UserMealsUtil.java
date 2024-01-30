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
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                        "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                        "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                        "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                        "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                        "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                        "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals,
                LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(
                filteredByStreams(meals,
                        LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(
                filteredByCyclesOptional(meals,
                        LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByStreamsOptional(meals,
                LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals,
                                                            LocalTime startTime,
                                                            LocalTime endTime,
                                                            int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDateMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            totalCaloriesByDateMap.put(userMeal.getDateTime().toLocalDate(),
                    totalCaloriesByDateMap.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) +
                            userMeal.getCalories());
        }

        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(
                        new UserMealWithExcess(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                new AtomicBoolean(
                                        totalCaloriesByDateMap.get(
                                                userMeal.getDateTime().toLocalDate()) > caloriesPerDay)));
            }
        }

        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals,
                                                             LocalTime startTime,
                                                             LocalTime endTime,
                                                             int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDateMap = meals
                .stream()
                .collect(Collectors.groupingBy(
                        userMeal1 -> userMeal1.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal ->
                        new UserMealWithExcess(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                new AtomicBoolean(
                                        totalCaloriesByDateMap.get(userMeal.getDateTime().toLocalDate()) >
                                                caloriesPerDay)))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional(List<UserMeal> meals,
                                                                    LocalTime startTime,
                                                                    LocalTime endTime,
                                                                    int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDateMap = new HashMap<>();
        Map<LocalDate, AtomicBoolean> isExcessByDateMap = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();

        for (UserMeal userMeal : meals) {
            MealUtil.fillMapsWithStatistics(caloriesPerDay, userMeal, totalCaloriesByDateMap, isExcessByDateMap);

            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(
                        new UserMealWithExcess(userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                isExcessByDateMap.get(userMeal.getDateTime().toLocalDate())));
            }
        }

        return userMealWithExcessList;
    }


    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals,
                                                                     LocalTime startTime,
                                                                     LocalTime endTime,
                                                                     int caloriesPerDay) {
        return meals.stream()
                .parallel()
                .collect(new UserMealWithExcessCollector(startTime, endTime, caloriesPerDay));
    }
}
