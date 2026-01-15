package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.ConstantSql;
import by.rabenok.webinnowise.dao.UserDao;
import by.rabenok.webinnowise.exception.ConnectionException;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;
import by.rabenok.webinnowise.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
  public static final Logger LOGGER = LogManager.getLogger();
  private static UserDaoImpl instance = new UserDaoImpl();

  private UserDaoImpl() {
  }

  public static UserDaoImpl getInstance() {
    return instance;
  }

  @Override
  public String authenticate(String login, String password) throws DaoException {
    String passFromDB = null;
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(ConstantSql.SELECT_PASSWORD_BY_USERNAME)) {
      statement.setString(1, login);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          passFromDB = resultSet.getString("password");
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error authenticating user: {}", login, e);
      throw new DaoException(e);
    }
    return passFromDB;
  }

  @Override
  public Optional<Role> authorize(String login) throws DaoException {
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = connection.prepareStatement(ConstantSql.SELECT_ROLE_BY_USERNAME)) {
      ps.setString(1, login);
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          Role role = Role.valueOf(resultSet.getString("role"));
          return Optional.of(role);
        }
      }
    } catch (SQLException | ConnectionException e) {
      LOGGER.error("Error authorize user by login: {}", login, e);
      throw new DaoException(e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> findUserByName(String userName) throws DaoException {
    User user = null;
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(ConstantSql.SELECT_USER_BY_USERNAME)) {
      statement.setString(1, userName);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int id = resultSet.getInt("id");
          String role = resultSet.getString("role");
          user = new User();
          user.setId(id);
          user.setName(userName);
          user.setRole(Role.valueOf(role));
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding user by name: {}", userName, e);
      throw new DaoException(e);
    }
    return Optional.ofNullable(user);
  }

  @Override
  public Optional<User> findUserById(int id) throws DaoException {
    User user = null;
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(ConstantSql.SELECT_USER_BY_ID)) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          String name = resultSet.getString("name");
          String role = resultSet.getString("role");
          user = new User();
          user.setId(id);
          user.setName(name);
          user.setRole(Role.valueOf(role));
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding user by id: {}", id, e);
      throw new DaoException(e);
    }
    return Optional.ofNullable(user);
  }
}