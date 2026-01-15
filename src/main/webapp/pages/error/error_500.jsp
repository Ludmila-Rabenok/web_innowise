<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 22.12.2025
  Time: 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>500</title>
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
<h2>Произошла внутренняя ошибка (500)</h2>
<div class="error-box"><p><strong>Сообщение:</strong> ${error_msg}</p>
    <p><strong>Тип ошибки:</strong> <%= exception != null ? exception.getClass().getName() : "N/A" %>
    </p>
    <p><strong>Сообщение:</strong> <%= exception.getMessage() %>
    </p>
</div>
<p><a href="index.jsp" class="btn">Вернуться на главную</a></p>
<br/><br/><br/>
</body>
</html>