package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String forward;
        String action = request.getParameter("action");

        String INSERT_OR_EDIT = "/meal.jsp";
        if (action.equalsIgnoreCase("delete")) {
            Integer mealId = Integer.parseInt(request.getParameter("mealId"));
            MealsUtil.mealMemoryRepository.delete(mealId);
            response.sendRedirect("meals");
            return;

        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            Integer mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = MealsUtil.mealMemoryRepository.get(mealId);
            request.setAttribute("meal", meal);
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");

        String description = request.getParameter("description");

        String caloriesParameter = request.getParameter("calories");
        int calories = 0;
        if (caloriesParameter != null && !caloriesParameter.isEmpty()) {
            calories = Integer.parseInt(caloriesParameter);
        }

        LocalDateTime mealDateTime =
                LocalDateTime.parse(request.getParameter("dateTime"), TimeUtil.readableDateTimeFormatter);

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(null, mealDateTime, description, calories);
            MealsUtil.mealMemoryRepository.create(meal);
        } else {
            Meal meal = new Meal(Integer.parseInt(mealId), mealDateTime, description, calories);
            MealsUtil.mealMemoryRepository.update(meal);
        }

        response.sendRedirect("meals");

    }
}
