<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 22.12.2025
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>MainClient</title>
    <style> body {
        font-family: Arial, sans-serif;
        margin: 40px;
    }

    h2 {
        color: #2e6da4;
    }

    .btn {
        display: inline-block;
        padding: 10px 20px;
        margin-top: 10px;
        background: #007bff;
        color: #fff;
        text-decoration: none;
        border-radius: 4px;
        border: none;
        cursor: pointer;
        font-size: 14px;
    }

    .btn:hover {
        background: #0056b3;
    }

    form {
        margin-bottom: 20px;
    }

    select, input[type="date"], input[type="time"] {
        padding: 6px;
        width: 200px;
    }
    </style>
</head>
<body>
<h2><c:out value="${user}"/>!</h2>
<!-- Кнопка "Список заявок" -->
<form action="controller" method="get">
    <input type="hidden" name="command" value="client_orders"/>
    <input type="submit" class="btn" value="Посмотреть список заявок"/>
</form>
<br/>
<!-- Кнопка "Подать заявку" -->
<h2>Записаться онлайн</h2>
<form action="controller" method="post">
    <input type="hidden" name="command" value="new_order"/>
    <!-- Набор услуг -->
    <label for="procedures">Выберите услугу:</label><br>
    <select name="procedures" id="procedures" multiple>
        <option value="haircut">Стрижка</option>
        <option value="nails">Маникюр</option>
        <option value="wash">Мойка</option>
    </select><br><br>
    <!-- Время выполнения -->
    <label for="date">Дата выполнения:</label><br>
    <input type="date" id="date" name="date"><br><br>
    <label for="time">Время выполнения:</label><br>
    <input type="time" id="time" name="time"><br><br>
    <input type="submit" class="btn" value="Отправить заявку">
    <c:if test="${not empty order_error}">
        <div style="color: #b30000; margin-top: 10px;">
            <c:out value="${order_error}"/></div>
    </c:if>
</form>
</body>
</html>
