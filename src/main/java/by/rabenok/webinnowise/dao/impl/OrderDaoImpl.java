package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.OrderDao;
import by.rabenok.webinnowise.exception.ConnectionException;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Procedure;
import by.rabenok.webinnowise.model.Status;
import by.rabenok.webinnowise.model.User;
import by.rabenok.webinnowise.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
  public static final Logger LOGGER = LogManager.getLogger();
  private static OrderDaoImpl instance = new OrderDaoImpl();

  private OrderDaoImpl() {
  }

  public static OrderDaoImpl getInstance() {
    return instance;
  }

  public List<Order> findOrdersFromUser(User user) throws DaoException {
    List<Order> orders = new ArrayList<>();
    String sql = "SELECT id, user_id, lead_time, status, bill FROM orders WHERE user_id = ?";
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, user.getId());
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          Order order = new Order();
          order.setId(rs.getInt("id"));
          order.setLeadTime(rs.getTimestamp("lead_time").toLocalDateTime());
          order.setStatus(Status.valueOf(rs.getString("status")));
          order.setBill(rs.getBigDecimal("bill"));
          order.setUser(user);
          order.setProcedures(findProceduresByOrderId(connection, order.getId()));
          orders.add(order);
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding orders for user: {}", user.getName(), e);
      throw new DaoException(e);
    }
    return orders;
  }

  @Override
  public void save(Order order) throws DaoException {
    String insertOrderSql = "INSERT INTO orders (user_id, lead_time, status, bill) VALUES (?, ?, ?, ?) RETURNING id";
    String insertOrderProcedureSql = "INSERT INTO order_procedure (order_id, procedure_id) VALUES (?, ?)";
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(insertOrderSql)) {
      statement.setInt(1, order.getUser().getId());
      statement.setTimestamp(2, Timestamp.valueOf(order.getLeadTime()));
      statement.setString(3, order.getStatus().name());
      statement.setBigDecimal(4, order.getBill());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int orderId = resultSet.getInt("id");
          order.setId(orderId);
          try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrderProcedureSql)) {
            for (Procedure procedure : order.getProcedures()) {
              preparedStatement.setInt(1, orderId);
              preparedStatement.setInt(2, procedure.getId());
              preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
          }
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error saving order", e);
      throw new DaoException(e);
    }
  }

  @Override
  public void update(Order order) throws DaoException {
    String updateOrderSql = "UPDATE orders SET user_id = ?, lead_time = ?, status = ?, bill = ? WHERE id = ?";
    String deleteProceduresSql = "DELETE FROM order_procedure WHERE order_id = ?";
    String insertProceduresSql = "INSERT INTO order_procedure (order_id, procedure_id) VALUES (?, ?)";
    try (Connection connection = ConnectionPool.getInstance().getConnection()) {
      try (PreparedStatement updateStmt = connection.prepareStatement(updateOrderSql)) {
        updateStmt.setInt(1, order.getUser().getId());
        updateStmt.setTimestamp(2, Timestamp.valueOf(order.getLeadTime()));
        updateStmt.setString(3, order.getStatus().name());
        updateStmt.setBigDecimal(4, order.getBill());
        updateStmt.setInt(5, order.getId());
        updateStmt.executeUpdate();
      }
      try (PreparedStatement deleteStmt = connection.prepareStatement(deleteProceduresSql)) {
        deleteStmt.setInt(1, order.getId());
        deleteStmt.executeUpdate();
      }
      try (PreparedStatement insertStmt = connection.prepareStatement(insertProceduresSql)) {
        for (Procedure procedure : order.getProcedures()) {
          insertStmt.setInt(1, order.getId());
          insertStmt.setInt(2, procedure.getId());
          insertStmt.addBatch();
        }
        insertStmt.executeBatch();
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error updating order with id: {}", order.getId(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public List<Order> findAll() throws DaoException {
    List<Order> orders = new ArrayList<>();
    String sql = "SELECT id, user_id, lead_time, status, bill FROM orders";
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setLeadTime(resultSet.getTimestamp("lead_time").toLocalDateTime());
        order.setStatus(Status.valueOf(resultSet.getString("status")));
        order.setBill(resultSet.getBigDecimal("bill"));
        int userId = resultSet.getInt("user_id");
        order.setUser(findUserById(connection, userId));
        order.setProcedures(findProceduresByOrderId(connection, order.getId()));
        orders.add(order);
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding all orders", e);
      throw new DaoException(e);
    }
    return orders;
  }

  @Override
  public Optional<Order> findById(int id) throws DaoException {
    Order order = null;
    String sql = "SELECT id, user_id, lead_time, status, bill FROM orders WHERE id = ?";
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          order = new Order();
          order.setId(rs.getInt("id"));
          order.setLeadTime(rs.getTimestamp("lead_time").toLocalDateTime());
          order.setStatus(Status.valueOf(rs.getString("status")));
          order.setBill(rs.getBigDecimal("bill"));
          int userId = rs.getInt("user_id");
          order.setUser(findUserById(connection, userId));
          order.setProcedures(findProceduresByOrderId(connection, order.getId()));
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding order by id: {}", id, e);
      throw new DaoException(e);
    }
    return Optional.ofNullable(order);
  }
}