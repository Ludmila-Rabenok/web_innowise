<%--
  Created by IntelliJ IDEA.
  User: Mila
  Date: 24.12.2025
  Time: 22:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>ClientOrders</title></head>
<body><h2>Мои заявки</h2>
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
        <tr> <!-- ID заявки -->
            <td>${order.id}</td>
            <!-- Список процедур с оценкой -->
            <td><c:forEach var="proc" items="${order.procedures}"> ${proc.name}
                <form action="controller" method="post" style="display:inline;">
                    <input type="hidden" name="command" value="evaluation"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <input type="hidden" name="procedureId" value="${proc.id}"/>
                    <!-- Оценка по 5-балльной системе -->
                    <label>Оценка:</label>
                    <select name="rating">
                        <option value="1">1 ⭐</option>
                        <option value="2">2 ⭐⭐</option>
                        <option value="3">3 ⭐⭐⭐</option>
                        <option value="4">4 ⭐⭐⭐⭐</option>
                        <option value="5">5 ⭐⭐⭐⭐⭐</option>
                    </select>
                    <input type="submit" value="Оценить"/>
                </form>
                <br/> </c:forEach></td>
            <!-- Дата и время выполнения -->
            <td>${order.leadTime}</td>
            <!-- Статус -->
            <td><c:choose> <c:when test="${order.status eq 'APPROVED'}">✅ Подтверждено</c:when> <c:when
                    test="${order.status eq 'REJECTED'}">❌ Отклонено</c:when> <c:when
                    test="${order.status eq 'MODERATION'}">⌛ На модерации</c:when> </c:choose></td>
            <!-- Счёт -->
            <td>${order.bill} руб.</td>
            <!-- Кнопки действий -->
            <td>
                <!-- Отмена заказа -->
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="remove_order"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <input type="submit" value="Отменить заказ"/>
                </form>
            </td>
        </tr>
    </c:forEach></table>
</body>
</html>