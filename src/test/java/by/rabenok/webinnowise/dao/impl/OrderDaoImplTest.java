package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Status;
import by.rabenok.webinnowise.model.User;
import by.rabenok.webinnowise.pool.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderDaoImplTest {

  @BeforeEach
  void resetTable() throws Exception {
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      Path path = Paths.get("src/test/resources/reset_db.sql");
      String sql = new String(Files.readAllBytes(path));
      statement.execute(sql);
    }
  }

  @Test
  void findOrdersByUserName_shouldReturnOrdersWithProcedures() throws Exception {
    List<Order> orders = OrderDaoImpl.getInstance().findOrdersByUserName("IrinaTest");
    Order first = orders.get(0);
    Order second = orders.get(1);

    assertEquals(2, orders.size());
    assertEquals(1, first.getId());
    assertEquals(2, first.getProcedures().size());
    assertEquals(2, second.getId());
    assertEquals(1, second.getProcedures().size());
  }

  @Test
  void save_shouldInsertOrderAndProcedures() throws Exception {
    User user = new User();
    user.setId(2);
    Order order = new Order();
    order.setUser(user);
    order.setLeadTime(LocalDateTime.of(2026, 1, 1, 12, 0));
    String[] procedures = {"haircut", "wash"};

    OrderDaoImpl.getInstance().save(order, procedures);

    Optional<Order> saved = OrderDaoImpl.getInstance().findById(order.getId());
    assertTrue(order.getId() > 0);
    assertTrue(saved.isPresent());
    assertEquals(2, saved.get().getProcedures().size());
  }

  @Test
  void updateStatusAndBill_shouldUpdateOrder() throws Exception {
    Optional<Order> optionalOrder = OrderDaoImpl.getInstance().findById(1);
    Order order = optionalOrder.get();
    order.setStatus(Status.APPROVED);
    order.setBill(new BigDecimal("99.99"));

    boolean updated = OrderDaoImpl.getInstance().updateStatusAndBill(order);

    Optional<Order> updatedOrder = OrderDaoImpl.getInstance().findById(1);
    assertTrue(optionalOrder.isPresent());
    assertTrue(updated);
    assertEquals(Status.APPROVED, updatedOrder.get().getStatus());
    assertEquals(new BigDecimal("99.99"), updatedOrder.get().getBill());
  }

  @Test
  void findAll_shouldReturnAllOrders() throws Exception {
    List<Order> orders = OrderDaoImpl.getInstance().findAll();

    assertEquals(3, orders.size());
  }

  @Test
  void findById_shouldReturnOrderWithProcedures() throws Exception {
    Optional<Order> optionalOrder = OrderDaoImpl.getInstance().findById(1);
    assertTrue(optionalOrder.isPresent());
    Order order = optionalOrder.get();
    assertEquals(1, order.getId());
    assertEquals(2, order.getProcedures().size());
  }

  @Test
  void remove_shouldDeleteOrder() throws Exception {
    boolean removed = OrderDaoImpl.getInstance().remove(1);
    assertTrue(removed);
    Optional<Order> opt = OrderDaoImpl.getInstance().findById(1);
    assertFalse(opt.isPresent());
  }
}