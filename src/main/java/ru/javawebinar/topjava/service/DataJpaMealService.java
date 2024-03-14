package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
@Profile(Profiles.DATAJPA)
public class DataJpaMealService extends MealService {

    public DataJpaMealService(MealRepository repository) {
        super(repository);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        Meal meal = repository.getWithUser(id, userId);
        return checkNotFoundWithId(meal, id);
    }
}

