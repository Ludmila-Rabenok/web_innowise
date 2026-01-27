<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 15.01.2026
  Time: 17:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.Timestamp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Все заявки</title>
    <style> body {
        font-family: Arial, sans-serif;
        margin: 40px;
    }

    h2 {
        color: #2e6da4;
    }

    table {
        border-collapse: collapse;
        width: 100%;
    }

    th, td {
        border: 1px solid #ccc;
        padding: 10px;
        vertical-align: top;
    }

    th {
        background: #f0f8ff;
    }

    .btn {
        display: inline-block;
        padding: 8px 16px;
        background: #007bff;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        text-decoration: none;
        font-size: 14px;
    }

    .btn:hover {
        background: #0056b3;
    }

    .rating-select {
        margin-top: 5px;
    } </style>
</head>
<body>
<h2>Список всех заявок</h2>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Процедуры</th>
        <th>Дата и время</th>
        <th>Статус</th>
        <th>Счёт</th>
        <th>Действия</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr> <!-- ID -->
            <td><c:out value="${order.id}"/></td>
            <!-- Список процедур -->
            <td>
                <c:forEach var="proc" items="${order.procedures}">
                    <c:choose>
                        <c:when test="${proc.name == 'haircut'}">Стрижка</c:when>
                        <c:when test="${proc.name == 'nails'}">Маникюр</c:when>
                        <c:when test="${proc.name == 'wash'}">Мойка</c:when>
                        <c:otherwise>${proc.name} </c:otherwise>
                    </c:choose>
                    <br/>
                </c:forEach>
            </td>
            <!-- Дата и время -->
            <td>
                <fmt:formatDate value="${Timestamp.valueOf(order.leadTime)}" pattern="dd MMMM yyyy, HH:mm"/>
            </td>
            <!-- Статус -->
            <td><c:choose> <c:when test="${order.status eq 'APPROVED'}">✅ Подтверждено</c:when> <c:when
                    test="${order.status eq 'REJECTED'}">❌ Отклонено</c:when> <c:when
                    test="${order.status eq 'MODERATION'}">⌛ На модерации</c:when> </c:choose>
            </td>
            <!-- Счёт -->
            <td>
                <c:choose>
                    <c:when test="${order.bill != null}">
                        <c:out value="${order.bill}"/> руб.
                    </c:when>
                    <c:otherwise> ожидает расчёта </c:otherwise>
                </c:choose>
            </td>
            <!-- Кнопки" -->
            <td>
                <!-- Одобрить заявку -->
                <form action="controller" method="post" style="display:inline;">
                    <input type="hidden" name="command" value="approve_order"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn">Одобрить</button>
                </form>
                <!-- Отклонить заявку -->
                <form action="controller" method="post" style="display:inline;">
                    <input type="hidden" name="command" value="reject_order"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn btn-red">Отклонить</button>
                </form>
            </td>
        </tr>
    </c:forEach></table>
<br/>
<br/>
<form action="controller" method="post">
    <input type="hidden" name="command" value="logout"/>
    <button type="submit" class="btn">Выйти</button>
</form>
</body>
</html>
