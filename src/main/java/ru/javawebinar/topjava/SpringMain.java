package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            MealRestController mealRestControllerController = appCtx.getBean(MealRestController.class);

            InMemoryMealRepository repository = appCtx.getBean(InMemoryMealRepository.class);

            List<Meal> meals = (List<Meal>) repository.getAll(1);
            meals.forEach(System.out::println);


            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            mealRestControllerController.getAll().forEach(System.out::println);

        }
    }
}
