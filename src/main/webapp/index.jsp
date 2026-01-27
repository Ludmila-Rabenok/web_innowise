<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
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

    .error {
        color: #b30000;
        margin-top: 10px;
    }

    input[type="text"], input[type="password"] {
        padding: 6px;
        width: 200px;
    } </style>
</head>
<body>
<br/>
<form action="controller" method="post">
    <input type="hidden" name="command" value="login"/>
    Login: <input type="text" name="login" value=""/><br/><br/>
    Password: <input type="password" name="pass" value=""/><br/><br/>
    <button type="submit" class="btn">Войти</button>
    <c:if test="${not empty login_msg}">
        <div class="error">
            <c:out value="${login_msg}"/>
        </div>
    </c:if>
</form>
</body>
</html>