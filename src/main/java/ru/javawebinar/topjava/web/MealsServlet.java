package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.Calorie_Norm;

public class MealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        log.debug("redirect to meals");
        List<MealTo> mealsTo =
                MealsUtil.filteredByStreams(MealsUtil.mealMemoryRepository.getAll(),
                        LocalTime.of(0, 0),
                        LocalTime.of(23, 59, 59),
                        Calorie_Norm);
        request.setAttribute("meals", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
