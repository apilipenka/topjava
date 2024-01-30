package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MealUtil {
    public static void fillMapsWithStatistics(int caloriesPerDay,
                                              UserMeal userMeal,
                                              Map<LocalDate, Integer> totalCaloriesByDateMap,
                                              Map<LocalDate, AtomicBoolean> isExcessByDateMap) {

        LocalDate userMealLocalDate = userMeal.getDateTime().toLocalDate();

        totalCaloriesByDateMap.merge(userMealLocalDate, userMeal.getCalories(), Integer::sum);

        AtomicBoolean isExcess = isExcessByDateMap.computeIfAbsent(userMealLocalDate,
                k -> new AtomicBoolean(totalCaloriesByDateMap.get(userMealLocalDate) > caloriesPerDay));
        isExcess.set(totalCaloriesByDateMap.get(userMealLocalDate) > caloriesPerDay);
    }
}
