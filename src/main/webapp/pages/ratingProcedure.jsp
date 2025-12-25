<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 25.12.2025
  Time: 20:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><title>Спасибо за оценку</title>
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
        margin: 10px;
        background: #007bff;
        color: #fff;
        text-decoration: none;
        border-radius: 4px;
    }

    .btn:hover {
        background: #0056b3;
    } </style>
</head>
<body><h2>Спасибо за вашу оценку!</h2>
<p>Ваш отзыв учтён. Вы можете продолжить работу:</p>
<!-- Кнопка "Вернуться к списку заявок" --> <a
        href="pages/clientOrders.jsp" class="btn">Вернуться к заявкам</a>
<!-- Кнопка "Выйти из системы" -->
<form action="controller" method="post" style="display:inline;">
    <input type="hidden" name="command" value="logout"/>
    <input type="submit" value="Выйти из системы" class="btn"/></form>
</body>
</html>