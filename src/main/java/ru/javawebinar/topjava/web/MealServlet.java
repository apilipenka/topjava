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
    public void init() throws ServletException {
        super.init();
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
                Integer.parseInt(request.getParameter("calories")),
                SecurityUtil.getAuthUserId());


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
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.getAuthUserId()) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter": {

                String filterDateFromAttribute = request.getParameter("filterDateFrom");
                LocalDate filterDateFrom;
                if (filterDateFromAttribute != null && !filterDateFromAttribute.isEmpty() &&
                        !filterDateFromAttribute.equalsIgnoreCase("null")) {
                    filterDateFrom = LocalDate.parse(filterDateFromAttribute);
                    request.getSession().setAttribute("filterDateFrom", filterDateFrom);
                } else {
                    request.getSession().removeAttribute("filterDateFrom");
                }

                String filterDateToAttribute = request.getParameter("filterDateTo");
                LocalDate filterDateTo;
                if (filterDateToAttribute != null && !filterDateToAttribute.isEmpty() &&
                        !filterDateToAttribute.equalsIgnoreCase("null")) {
                    filterDateTo = LocalDate.parse(filterDateToAttribute);
                    request.getSession().setAttribute("filterDateTo", filterDateTo);
                } else {
                    request.getSession().removeAttribute("filterDateTo");
                }

                String filterTimeFromAttribute = request.getParameter("filterTimeFrom");
                LocalTime filterTimeFrom;
                if (filterTimeFromAttribute != null && !filterTimeFromAttribute.isEmpty() &&
                        !filterTimeFromAttribute.equalsIgnoreCase("null")) {
                    filterTimeFrom = LocalTime.parse(filterTimeFromAttribute);
                    request.getSession().setAttribute("filterTimeFrom", filterTimeFrom);
                } else {
                    request.getSession().removeAttribute("filterTimeFrom");
                }

                String filterTimeToAttribute = request.getParameter("filterTimeTo");
                LocalTime filterTimeTo;
                if (filterTimeToAttribute != null && !filterTimeToAttribute.isEmpty() &&
                        !filterTimeToAttribute.equalsIgnoreCase("null")) {
                    filterTimeTo = LocalTime.parse(filterTimeToAttribute);
                    request.getSession().setAttribute("filterTimeTo", filterTimeTo);
                } else {
                    request.getSession().removeAttribute("filterTimeTo");
                }

                response.sendRedirect("meals");
                break;
            }
            case "all":
            default:
                String filterDateFromAttribute = String.valueOf(request.getSession().getAttribute("filterDateFrom"));
                LocalDate filterDateFrom = null;
                if (filterDateFromAttribute != null && !filterDateFromAttribute.isEmpty() &&
                        !filterDateFromAttribute.equalsIgnoreCase("null")) {
                    filterDateFrom = LocalDate.parse(filterDateFromAttribute);

                }

                String filterDateToAttribute = String.valueOf(request.getSession().getAttribute("filterDateTo"));
                LocalDate filterDateTo = null;
                if (filterDateToAttribute != null && !filterDateToAttribute.isEmpty() &&
                        !filterDateToAttribute.equalsIgnoreCase("null")) {
                    filterDateTo = LocalDate.parse(filterDateToAttribute);

                }

                String filterTimeFromAttribute = String.valueOf(request.getSession().getAttribute("filterTimeFrom"));
                LocalTime filterTimeFrom = null;
                if (filterTimeFromAttribute != null && !filterTimeFromAttribute.isEmpty() &&
                        !filterTimeFromAttribute.equalsIgnoreCase("null")) {
                    filterTimeFrom = LocalTime.parse(filterTimeFromAttribute);

                }

                String filterTimeToAttribute = String.valueOf(request.getSession().getAttribute("filterTimeTo"));
                LocalTime filterTimeTo = null;
                if (filterTimeToAttribute != null && !filterTimeToAttribute.isEmpty() &&
                        !filterTimeToAttribute.equalsIgnoreCase("null")) {
                    filterTimeTo = LocalTime.parse(filterTimeToAttribute);

                }


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
}
