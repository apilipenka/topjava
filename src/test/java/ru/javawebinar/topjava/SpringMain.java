package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)

        System.setProperty("spring.profiles.active", "memory");

        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml",
                "spring/spring-db.xml")) {

            ConfigurableEnvironment env = appCtx.getEnvironment();

            env.setActiveProfiles("jdbc");

            appCtx.setEnvironment(env);


            Arrays.stream(appCtx.getBeanDefinitionNames()).sorted().forEach(System.out::println);

            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            try {
                adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println();

            MealRestController mealController = appCtx.getBean(MealRestController.class);
            List<MealTo> filteredMealsWithExcess =
                    mealController.getBetween(
                            LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
            filteredMealsWithExcess.forEach(System.out::println);
            System.out.println();
            System.out.println(mealController.getBetween(null, null, null, null));
        }
    }
}
