<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 24.12.2025
  Time: 22:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.Timestamp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>ClientOrders</title>
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
<h2>Мои заявки</h2>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>Процедуры</th>
        <th>Оценка процедуры</th>
        <th>Дата и время</th>
        <th>Статус</th>
        <th>Счёт</th>
        <th>Отмена заявки</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
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
            <!-- Оценки -->
            <td>
                <c:forEach var="proc" items="${order.procedures}">
                    <!-- Средняя оценка -->
                    <c:choose>
                        <c:when test="${proc.ratingAverage != null}"> ⭐ Средняя: ${proc.ratingAverage} </c:when>
                        <c:otherwise> ⭐ Нет оценок </c:otherwise>
                    </c:choose>
                    <!-- Форма оценки -->
                    <form action="controller" method="post" style="display:inline;">
                        <input type="hidden" name="command" value="evaluation"/>
                        <input type="hidden" name="orderId" value="${order.id}"/>
                        <input type="hidden" name="procedureId" value="${proc.id}"/>
                        <select name="rating">
                            <option value="1">1 ⭐</option>
                            <option value="2">2 ⭐⭐</option>
                            <option value="3">3 ⭐⭐⭐</option>
                            <option value="4">4 ⭐⭐⭐⭐</option>
                            <option value="5">5 ⭐⭐⭐⭐⭐</option>
                        </select>
                        <button type="submit" class="btn">Оценить</button>
                    </form>
                    <br/>
                    <br/>
                </c:forEach>
            </td>
            <!-- Дата и время выполнения -->
            <td>
                <fmt:formatDate value="${Timestamp.valueOf(order.leadTime)}" pattern="dd MMMM yyyy, HH:mm"/>
            </td>
            <!-- Статус -->
            <td>
                <c:choose>
                    <c:when test="${order.status eq 'APPROVED'}">✅ Подтверждено</c:when>
                    <c:when test="${order.status eq 'REJECTED'}">❌ Отклонено</c:when>
                    <c:when test="${order.status eq 'MODERATION'}">⌛ На модерации</c:when>
                </c:choose>
            </td>
            <!-- Счёт -->
            <td>
                <c:choose>
                    <c:when test="${order.bill != null}"> ${order.bill} руб. </c:when>
                    <c:otherwise> ожидает расчёта </c:otherwise>
                </c:choose>
            </td>
            <!-- Кнопки действий -->
            <td>
                <!-- Отмена заявки -->
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="remove_order"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn">Отменить</button>
                </form>
            </td>
        </tr>
    </c:forEach></table>
<br/><br/>
<form action="controller" method="post">
    <input type="hidden" name="command" value="logout"/>
    <button type="submit" class="btn">Выйти</button>
</form>
</body>
</html>