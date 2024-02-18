package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestUtils;
import ru.javawebinar.topjava.TestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.TestData.*;

@ActiveProfiles(value = "jdbc")
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(ADMIN_LUNCH_ID, ADMIN_ID);
        MealTestUtils.assertMatch(meal, TestData.adminLunch);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getNotFoundForWrongUser() {
        assertThrows(NotFoundException.class, () -> service.get(USER_LUNCH_ID, ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        MealTestUtils.assertMatch(all, adminDinner, adminLunch);
    }

    @Test
    public void getAllWrongUser() {
        List<Meal> all = service.getAll(123);
        assertNotEquals(null, all);
        assertTrue(all.isEmpty());
    }

    @Test
    public void update() {
        Meal updated = MealTestUtils.getUpdated(adminLunch);
        service.update(updated, ADMIN_ID);
        MealTestUtils.assertMatch(service.get(ADMIN_LUNCH_ID, ADMIN_ID), MealTestUtils.getUpdated(adminLunch));
    }

    @Test
    public void updateNotFoundForWrongUser() {
        Meal updated = MealTestUtils.getUpdated(adminLunch);
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestUtils.getNew(), ADMIN_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestUtils.getNew();
        newMeal.setId(newId);
        MealTestUtils.assertMatch(created, newMeal);
        MealTestUtils.assertMatch(service.get(newId, ADMIN_ID), newMeal);
    }

    @Test
    public void duplicateCreate() {
        Meal newMeal = MealTestUtils.getNew();
        newMeal.setDateTime(adminLunch.getDateTime());
        assertThrows(DataAccessException.class, () ->
                service.create(newMeal, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2020, 1, 30), LocalDate.of(2020, 1, 31), USER_ID);
        MealTestUtils.assertMatch(meals, userSecondDinner, userSecondLunch, userSecondBreakfast,
                userDinner, userLunch, userBreakfast);
    }

    @Test
    public void getBetweenInclusiveStartDateNullAndEndDateNull() {
        List<Meal> meals = service.getBetweenInclusive(null, null, USER_ID);
        MealTestUtils.assertMatch(meals, userSecondDinner, userSecondLunch, userSecondBreakfast,
                userDinner, userLunch, userBreakfast);
    }

    @Test
    public void getBetweenInclusiveStartDateNotNullAndEndDateNull() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2020, 1, 31), null, USER_ID);
        MealTestUtils.assertMatch(meals, userSecondDinner, userSecondLunch, userSecondBreakfast);
    }

    @Test
    public void getBetweenInclusiveStartDateNullAndEndDateNotNull() {
        List<Meal> meals = service.getBetweenInclusive(null, LocalDate.of(2020, 1, 30), USER_ID);
        MealTestUtils.assertMatch(meals, userDinner, userLunch, userBreakfast);
    }

    @Test
    public void getBetweenInclusiveWrongUser() {
        List<Meal> meals = service.getBetweenInclusive(null, null, 123);
        assertNotEquals(null, meals);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void delete() {
        service.delete(ADMIN_LUNCH_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_LUNCH_ID, ADMIN_ID));
    }

    @Test
    public void deleteForWrongUser() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_LUNCH_ID, USER_ID));
    }
}