package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfNextDayOrMax;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
@Profile("!"+ Profiles.DATAJPA)
public class MealService implements MealServiceInt {

    protected final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public List<Meal> getBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public void update(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        checkNotFoundWithId(repository.save(meal, userId), meal.id());
    }

    @Override
    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(meal, userId);
    }
}