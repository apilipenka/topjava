package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIE_NORM;
import static ru.javawebinar.topjava.util.TimeUtil.READABLE_DATETIME_FORMATTER;

public class MealsServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT_URL = "/meal.jsp";
    private static final String LIST_URL = "/meals.jsp";
    private static final Logger log = getLogger(MealsServlet.class);
    private MemoryMealRepository memoryMealRepository;

    @Override
    public void init() {
        memoryMealRepository = new MemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");

        switch (action == null ? "" : action.toLowerCase()) {
            case "delete": {
                log.debug("doGet() redirect to meals");
                Integer mealId = Integer.parseInt(request.getParameter("mealId"));
                memoryMealRepository.delete(mealId);
                response.sendRedirect("meals");
                return;
            }
            case "edit": {
                forward = INSERT_OR_EDIT_URL;
                Integer mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = memoryMealRepository.get(mealId);
                request.setAttribute("meal", meal);
                request.setAttribute("formatter", READABLE_DATETIME_FORMATTER);
                break;
            }
            case "insert": {
                forward = INSERT_OR_EDIT_URL;
                break;
            }
            default: {
                forward = LIST_URL;
                List<MealTo> mealsTo =
                        MealsUtil.filteredByStreams(memoryMealRepository.getAll(),
                                LocalTime.MIN,
                                LocalTime.MAX,
                                CALORIE_NORM);
                request.setAttribute("meals", mealsTo);
                request.setAttribute("formatter", READABLE_DATETIME_FORMATTER);
            }
        }

        log.debug(String.format("doGet() forward to %s", forward));
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime mealDateTime =
                LocalDateTime.parse(request.getParameter("dateTime"), READABLE_DATETIME_FORMATTER);
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(null, mealDateTime, description, calories);
            memoryMealRepository.create(meal);
        } else {
            Meal meal = new Meal(Integer.parseInt(mealId), mealDateTime, description, calories);
            memoryMealRepository.update(meal);
        }
        log.debug("doPost() redirect to meals");
        response.sendRedirect("meals");
    }

}
