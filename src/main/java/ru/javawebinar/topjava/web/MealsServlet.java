package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealMemoryRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIE_NORM;
import static ru.javawebinar.topjava.util.TimeUtil.READABLE_DATETIME_FORMATTER;

public class MealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsServlet.class);
    MealMemoryRepository mealMemoryRepository = MealMemoryRepository.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");
        List<MealTo> mealsTo =
                MealsUtil.filteredByStreams(mealMemoryRepository.getAll(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        CALORIE_NORM);
        request.setAttribute("meals", mealsTo);
        request.setAttribute("formatter", READABLE_DATETIME_FORMATTER);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
