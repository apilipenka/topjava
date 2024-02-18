package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class TestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;
    public static final int ADMIN_LUNCH_ID = START_SEQ + 3;
    public static final int ADMIN_DINNER_ID = START_SEQ + 4;
    public static final int USER_BREAKFAST_ID = START_SEQ + 5;
    public static final int USER_LUNCH_ID = START_SEQ + 6;
    public static final int USER_DINNER_ID = START_SEQ + 7;
    public static final int USER_SECOND_BREAKFAST_ID = START_SEQ + 8;
    public static final int USER_SECOND_LUNCH_ID = START_SEQ + 9;
    public static final int USER_SECOND_DINNER_ID = START_SEQ + 10;
    public static final int NOT_FOUND = 10;
    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
    public static final User guest = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest");
    public static final Meal adminLunch =
            new Meal(ADMIN_LUNCH_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0),
                    "Админ ланч", 510);
    public static final Meal adminDinner =
            new Meal(ADMIN_DINNER_ID, LocalDateTime.of(2015, Month.JUNE, 1, 20, 0),
                    "Админ ужин", 610);
    public static final Meal userBreakfast =
            new Meal(USER_BREAKFAST_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                    "Пользователь Завтрак", 1456);
    public static final Meal userLunch =
            new Meal(USER_LUNCH_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                    "Пользователь Обед", 1234);
    public static final Meal userDinner =
            new Meal(USER_DINNER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                    "Пользователь Ужин", 321);

    public static final Meal userSecondBreakfast =
            new Meal(USER_SECOND_BREAKFAST_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 11, 33),
                    "Пользователь Завтрак", 453);
    public static final Meal userSecondLunch =
            new Meal(USER_SECOND_LUNCH_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 12, 45),
                    "Пользователь Обед", 1500);
    public static final Meal userSecondDinner =
            new Meal(USER_SECOND_DINNER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 21, 15),
                    "Пользователь Ужин", 200);
}
