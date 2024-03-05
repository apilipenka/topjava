package ru.javawebinar.topjava.service;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.datajpa.DataJpaMealRepository;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
@Profile(Profiles.DATAJPA)
public class DataJpaMealService extends MealService {

    public DataJpaMealService(MealRepository repository) {
        super(repository);
    }

    public Meal getWithUser(int id, int userId) throws Exception {

        DataJpaMealRepository dataJpaMealRepository = getTargetObject(repository, DataJpaMealRepository.class);

        Meal meal = dataJpaMealRepository.getWithUser(id, userId);
        return checkNotFoundWithId(meal, id);
    }

    @SuppressWarnings({"unchecked"})
    protected <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return (T) ((Advised) proxy).getTargetSource().getTarget();
        } else {
            return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
        }
    }

}

