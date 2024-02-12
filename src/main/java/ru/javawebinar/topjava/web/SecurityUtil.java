package ru.javawebinar.topjava.web;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static final AtomicInteger authenticatedUserId = new AtomicInteger(1);

    public static int authUserId() {
        return authenticatedUserId.get();
    }

    public static void setAuthenticatedUserId(int authenticatedUserId) {
        SecurityUtil.authenticatedUserId.set(authenticatedUserId);
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}