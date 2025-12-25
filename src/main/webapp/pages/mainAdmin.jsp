<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 24.12.2025
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Все заявки</title></head>
<body><h2>Список всех заявок</h2>
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
            <td>${order.id}</td>
            <!-- Список процедур -->
            <td><c:forEach var="proc" items="${order.procedures}"> ${proc.name}<br/> </c:forEach></td>
            <!-- Дата и время -->
            <td>${order.leadTime}</td>
            <!-- Статус -->
            <td><c:choose> <c:when test="${order.status eq 'APPROVED'}">✅ Подтверждено</c:when> <c:when
                    test="${order.status eq 'REJECTED'}">❌ Отклонено</c:when> <c:when
                    test="${order.status eq 'MODERATION'}">⌛ На модерации</c:when> </c:choose>
            </td>
            <!-- Счёт -->
            <td>${order.bill} руб.</td>
            <!-- Кнопки" -->
            <td>
                <!-- Одобрить заявку -->
                <form action="controller" method="post" style="display:inline;">
                    <input type="hidden" name="command" value="approve_order"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <input type="submit" value="Одобрить"/>
                </form>
                <!-- Отклонить заявку -->
                <form action="controller" method="post" style="display:inline;">
                    <input type="hidden" name="command" value="reject_order"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <input type="submit" value="Отклонить"/>
                </form>
            </td>
        </tr>
    </c:forEach></table>
</body>
</html>