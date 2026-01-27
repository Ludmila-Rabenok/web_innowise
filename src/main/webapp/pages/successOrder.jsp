<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 15.01.2026
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Заявка зарегистрирована</title>
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
        margin: 10px 10px 0 0;
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
        display: inline;
    } </style>
</head>
<body><h2>Ваша заявка успешно зарегистрирована!</h2>
<p>Вы можете продолжить:</p>
<!-- Кнопка "Перейти к списку заявок" -->
<a href="controller?command=client_orders" class="btn">Перейти к заявкам</a>
<!-- Кнопка "Выйти из системы" -->
<form action="controller" method="post">
    <input type="hidden" name="command" value="logout"/>
    <button type="submit" class="btn">Выйти из системы</button>
</form>
</body>
</html>