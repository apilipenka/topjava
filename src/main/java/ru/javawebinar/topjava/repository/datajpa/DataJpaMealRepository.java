package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew()) {
            Meal mealFromDb;
            mealFromDb = get(meal.getId(), userId);
            if (mealFromDb == null) {
                return null;
            }
            meal.setUser(mealFromDb.getUser());
        } else {
            User user = crudUserRepository.getReferenceById(userId);
            meal.setUser(user);
        }
        return crudMealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        try {
            Query q = this.entityManager.createQuery("SELECT m FROM Meal m JOIN FETCH m.user u WHERE m.id = :id " +
                    "and u.id=:userId order by m.dateTime desc ");
            q.setParameter("id", id);
            q.setParameter("userId", userId);
            return (Meal) q.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
