<%@ page contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>

<html lang="ru">
<head>
    <title>Meal</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>
<body>

<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meal</h2>

<form method="POST" action='meals' name="frmAddMeal" accept-charset="UTF-8">
    <label>
        ID :
        <input
                type="text" readonly="readonly" name="mealId"
                value="<c:out value="${meal.id}" />"/>
    </label> <br/>
    <label>
        Date :
        <input
                type="text" name="dateTime" placeholder="yyyy-MM-dd HH:mm"
                value="${formatter.format(meal.getDateTime())}"/>
    </label> <br/>
    <label>
        Description :
        <input
                type="text" name="description"
                value="<c:out value="${meal.description}" />"/>
    </label> <br/>
    <label>
        Calories :
        <input
                type="number" name="calories"
                value="<c:out value="${meal.calories}" />"/>
    </label> <br/>
    <p>
        <input type="submit" value="Submit"/>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </p>
</form>
</body>
</html>