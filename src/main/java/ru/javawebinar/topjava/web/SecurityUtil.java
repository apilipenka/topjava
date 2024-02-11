package ru.javawebinar.topjava.web;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static AtomicInteger authUserId = new AtomicInteger(1);

    public static int getAuthUserId() {
        return authUserId.get();
    }

    public static void setAuthUserId(int authUserId) {
        SecurityUtil.authUserId.set(authUserId);
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}