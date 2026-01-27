package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.ConstantSql;
import by.rabenok.webinnowise.dao.OrderDao;
import by.rabenok.webinnowise.exception.ConnectionException;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Procedure;
import by.rabenok.webinnowise.model.Role;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
  public static final Logger LOGGER = LogManager.getLogger();
  private static OrderDaoImpl instance = new OrderDaoImpl();

  private OrderDaoImpl() {
  }

  public static OrderDaoImpl getInstance() {
    return instance;
  }

  public List<Order> findOrdersByUserName(String userName) throws DaoException {
    Map<Integer, Order> map = new LinkedHashMap<>();
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = connection.prepareStatement(ConstantSql.SELECT_ORDERS_BY_USERNAME)) {
      ps.setString(1, userName);
      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          int orderId = resultSet.getInt("order_id");
          Order order = map.get(orderId);
          if (order == null) {
            order = buildOrder(resultSet);
            map.put(orderId, order);
          }
          Procedure procedure = buildProcedure(resultSet);
          if (procedure != null) {
            order.getProcedures().add(procedure);
          }
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding orders for user {}", userName, e);
      throw new DaoException(e);
    }
    return new ArrayList<>(map.values());
  }

  @Override
  public void save(Order order, String[] procedures) throws DaoException {
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(ConstantSql.INSERT_ORDERS)) {
      statement.setInt(1, order.getUser().getId());
      statement.setTimestamp(2, Timestamp.valueOf(order.getLeadTime()));
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int orderId = resultSet.getInt("id");
          order.setId(orderId);
          try (PreparedStatement preparedStatement = connection.prepareStatement
                  (ConstantSql.INSERT_ORDER_PROCEDURE)) {
            for (String procedureName : procedures) {
              preparedStatement.setInt(1, orderId);
              preparedStatement.setString(2, procedureName);
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
  public void updateStatusAndBill(Order order) throws DaoException {
    try (Connection connection = ConnectionPool.getInstance().getConnection()) {
      try (PreparedStatement updateStmt = connection.prepareStatement(ConstantSql.UPDATE_ORDER)) {
        updateStmt.setString(1, order.getStatus().name());
        updateStmt.setBigDecimal(2, order.getBill());
        updateStmt.setInt(3, order.getId());
        updateStmt.executeUpdate();
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error updating order with id: {}", order.getId(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public List<Order> findAll() throws DaoException {
    Map<Integer, Order> map = new HashMap<>();
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(ConstantSql.SELECT_ORDERS);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        int orderId = resultSet.getInt("order_id");
        Order order = map.get(orderId);
        if (order == null) {
          order = buildOrder(resultSet);
          map.put(orderId, order);
        }
        Procedure procedure = buildProcedure(resultSet);
        if (procedure != null) {
          order.getProcedures().add(procedure);
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding all orders", e);
      throw new DaoException(e);
    }
    return new ArrayList<>(map.values());
  }

  @Override
  public Optional<Order> findById(int id) throws DaoException {
    Order order = null;
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(ConstantSql.SELECT_ORDERS_BY_ID)) {
      preparedStatement.setInt(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          if (order == null) {
            order = buildOrder(resultSet);
          }
          Procedure procedure = buildProcedure(resultSet);
          if (procedure != null) {
            order.getProcedures().add(procedure);
          }
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding order by id: {}", id, e);
      throw new DaoException(e);
    }
    return Optional.ofNullable(order);
  }

  @Override
  public boolean remove(int id) throws DaoException {
    boolean result;
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(ConstantSql.DELETE_ORDER_BY_ID)) {
      preparedStatement.setInt(1, id);
      int executeUpdate = preparedStatement.executeUpdate();
      result = executeUpdate == 1;
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error removed order by id: {}", id, e);
      throw new DaoException(e);
    }
    return result;
  }

  private Order buildOrder(ResultSet resultSet) throws SQLException {
    Order order = new Order();
    order.setId(resultSet.getInt("order_id"));
    order.setLeadTime(resultSet.getTimestamp("order_lead_time").toLocalDateTime());
    order.setStatus(Status.valueOf(resultSet.getString("order_status")));
    order.setBill(resultSet.getBigDecimal("order_bill"));
    order.setUser(buildUser(resultSet));
    order.setProcedures(new ArrayList<>());
    return order;
  }

  private User buildUser(ResultSet resultSet) throws SQLException {
    User user = new User();
    user.setId(resultSet.getInt("user_id"));
    user.setName(resultSet.getString("user_name"));
    user.setRole(Role.valueOf(resultSet.getString("user_role")));
    return user;
  }

  private Procedure buildProcedure(ResultSet resultSet) throws SQLException {
    int procedureId = resultSet.getInt("procedure_id");
    if (resultSet.wasNull()) {
      return null;
    }
    Procedure procedure = new Procedure();
    procedure.setId(procedureId);
    procedure.setName(resultSet.getString("procedure_name"));
    procedure.setPrice(resultSet.getBigDecimal("procedure_price"));
    double avg = resultSet.getDouble("rating_average");
    if (resultSet.wasNull()) {
      procedure.setRatingAverage(null);
    } else {
      procedure.setRatingAverage(avg);
    }
    procedure.setRatingCount(resultSet.getInt("rating_count"));
    return procedure;
  }
}