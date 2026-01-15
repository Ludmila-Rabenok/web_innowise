<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 24.12.2025
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><title>Администратор — панель управления</title>
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
        margin-top: 15px;
        background: #007bff;
        color: #fff;
        text-decoration: none;
        border-radius: 4px;
    }

    .btn:hover {
        background: #0056b3;
    } </style>
</head>
<body><h2>Добро пожаловать, ${user}!</h2>
<p>Вы находитесь в панели администратора. Вы можете перейти к управлению заказами:</p> <a
        href="controller?command=admin_orders" class="btn">Перейти к заказам</a></body>
</html>