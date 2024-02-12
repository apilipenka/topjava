package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));


        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            mealRestController.create(meal);
        } else {
            mealRestController.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                LocalDate filterDateFrom = getLocalDateFilterParameter(request, "filterDateFrom");
                LocalDate filterDateTo = getLocalDateFilterParameter(request, "filterDateTo");
                LocalTime filterTimeFrom = getLocalTimeFilterParameter(request, "filterTimeFrom");
                LocalTime filterTimeTo = getLocalTimeFilterParameter(request, "filterTimeTo");
                if (filterDateFrom == null && filterDateTo == null && filterTimeFrom == null && filterTimeTo == null) {
                    log.info("getAll");
                    request.setAttribute("meals",
                            mealRestController.getAll());
                } else {
                    log.info("getAllFiltered");
                    request.setAttribute("meals",
                            mealRestController.getAllFilteredByDateAndByTime(filterDateFrom, filterDateTo,
                                    filterTimeFrom, filterTimeTo));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalDate getLocalDateFilterParameter(HttpServletRequest request, String paramName) {
        String filterDateFromAttribute = request.getParameter(paramName);
        if (filterDateFromAttribute != null && !filterDateFromAttribute.isEmpty() &&
                !filterDateFromAttribute.equalsIgnoreCase("null")) {
            return LocalDate.parse(filterDateFromAttribute);
        } else {
            return null;
        }
    }

    private LocalTime getLocalTimeFilterParameter(HttpServletRequest request, String paramName) {
        String filterDateFromParameter = request.getParameter(paramName);
        if (filterDateFromParameter != null && !filterDateFromParameter.isEmpty() &&
                !filterDateFromParameter.equalsIgnoreCase("null")) {
            return LocalTime.parse(filterDateFromParameter);
        } else {
            return null;
        }
    }
}
