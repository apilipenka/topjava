<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            width: 700px;
            padding: 5px;
        }

        .green {
            color: green
        }

        .red {
            color: red
        }

        .description {
            width: 50%;
        }

        .time {
            width: 30%;
        }

        .calories {
            text-align: right;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <thead>
    <tr>
        <th class="time">Время</th>
        <th class="description">Описание</th>
        <th>Калории</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="meal" items="${meals}">
        <tr class="${meal.excess ? 'red':'green'}">
            <td class="time">
                    ${formatter.format(meal.getDateTime())}
            </td>
            <td class="description">
                    ${meal.description}
            </td>
            <td class="calories">
                    ${meal.calories}
            </td>
            <td><a href="meals?action=edit&mealId=${meal.id}">Редактировать</a></td>
            <td><a href="meals?action=delete&mealId=${meal.id}">Удалить</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Добавить</a></p>
</body>
</html>