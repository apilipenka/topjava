package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.datajpa.DataJpaMealRepositoryInt;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
@Profile(Profiles.DATAJPA)
public class DataJpaMealService extends MealService implements DataJpaMealServiceInt {
    private final DataJpaMealRepositoryInt repository;

    public DataJpaMealService(DataJpaMealRepositoryInt repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        Meal meal = repository.getWithUser(id, userId);
        return checkNotFoundWithId(meal, id);
    }
}

