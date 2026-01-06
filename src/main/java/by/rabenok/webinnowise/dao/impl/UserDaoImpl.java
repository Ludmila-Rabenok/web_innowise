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
  String SQL_USER_BY_LOGIN = "SELECT id, name, role FROM users WHERE name = ?";

  private UserDaoImpl() {
  }

  public static UserDaoImpl getInstance() {
    return instance;
  }

  @Override
  public Optional<Role> authenticate(String login, String password) throws DaoException {
    String sql = "SELECT role FROM users WHERE name = ? AND password = ?";
    Role role = null;
    try (Connection connection = ConnectionPool.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, login);
      statement.setString(2, password);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          role = Role.valueOf(rs.getString("role"));
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error authenticating user: {}", login, e);
      throw new DaoException(e);
    }
    return Optional.ofNullable(role);


    boolean match = false;
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(ConstantSql.SELECT_PASSWORD)) {
      statement.setString(1, login);
      try (ResultSet resultSet = statement.executeQuery()) {
        String passFromDb;
        if (resultSet.next()) {
          passFromDb = resultSet.getString(1);
          match = password.equals(passFromDb);
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
    return Optional.of(Role.CLIENT);
  }

  @Override
  public Optional<User> findUserByLogin(String login) throws DaoException {
    User user = null;
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(SQL_USER_BY_LOGIN)) {
      statement.setString(1, login);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int id = resultSet.getInt("id");
          String name = resultSet.getString("name");
          String role = resultSet.getString("role");
          user = new User();
          user.setId(id);
          user.setName(name);
          user.setRole(Role.valueOf(role));
        }
      }
    } catch (ConnectionException | SQLException e) {
      LOGGER.error("Error finding user by login: {}", login, e);
      throw new DaoException(e);
    }
    return Optional.ofNullable(user);
  }
}