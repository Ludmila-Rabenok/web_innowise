<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 25.12.2025
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404</title>
    <style> body {
        font-family: Arial, sans-serif;
        margin: 40px;
    }

    h2 {
        color: #b30000;
    }
    </style>
</head>
<body>
<h2>Ошибка 404 — страница не найдена</h2>
<div class="error-box"><p><strong>Сообщение:</strong> ${error_msg}</p>
    <p>Запрошенный ресурс не существует или был перемещён.</p></div>
<p><a href="index.jsp" class="btn">Вернуться на главную</a></p>
</body>
</html>