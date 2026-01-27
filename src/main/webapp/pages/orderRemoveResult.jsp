<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 23.01.2026
  Time: 19:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Удаление заявки</title>
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
        border: none;
        cursor: pointer;
        font-size: 14px;
    }

    .btn:hover {
        background: #0056b3;
    } </style>
</head>
<body>
<h2><c:out value="${msg}"/></h2>
<form action="controller" method="get">
    <input type="hidden" name="command" value="client_orders"/>
    <button type="submit" class="btn">Вернуться ко всем заявкам</button>
</form>
</body>
</html>
