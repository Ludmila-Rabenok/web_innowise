<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 22.12.2025
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MainClient</title>
</head>
<body>
<h2>Hello ${user}</h2>
<!-- Кнопка "Список заявок" -->
<form action="controller" method="get">
    <input type="hidden" name="command" value="client_orders"/>
    <input type="submit" value="Посмотреть список моих заявок"/>
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
        <option value="nails">Обрезание когтей</option>
        <option value="wash">Мытье</option>
    </select><br><br>
    <!-- Время выполнения -->
    <label for="date">Дата выполнения:</label><br>
    <input type="date" id="date" name="date"><br><br>
    <label for="time">Время выполнения:</label><br>
    <input type="time" id="time" name="time"><br><br>
    <input type="submit" value="Отправить заявку">
</form>
</body>
</html>
