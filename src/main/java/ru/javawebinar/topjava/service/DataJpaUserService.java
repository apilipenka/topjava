package ru.javawebinar.topjava.service;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.datajpa.DataJpaMealRepository;
import ru.javawebinar.topjava.repository.datajpa.DataJpaUserRepository;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
@Profile(Profiles.DATAJPA)
public class DataJpaUserService extends UserService {
    public DataJpaUserService(UserRepository repository) {
        super(repository);
    }

    public User getWithMeal(int id) throws Exception {

        DataJpaUserRepository dataJpaMealRepository = getTargetObject(repository, DataJpaUserRepository.class);

        User user = dataJpaMealRepository.getWithMeal(id);
        return checkNotFoundWithId(user, id);
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