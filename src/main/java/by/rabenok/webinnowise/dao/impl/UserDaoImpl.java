package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.UserDao;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;

import java.util.Optional;

public class UserDaoImpl implements UserDao {
  private static UserDaoImpl instance = new UserDaoImpl();

  private UserDaoImpl() {
  }

  public static UserDaoImpl getInstance() {
    return instance;
  }

  @Override
  public Optional<Role> authenticate(String login, String password) throws DaoException {
//    boolean match = false;
//
//    try (Connection connection = ConnectionPool.getInstance().getConnection();
//         PreparedStatement statement = connection.prepareStatement(ConstantSql.SELECT_LOGIN_PASSWORD)) {
//      statement.setString(1, login);
//      try (ResultSet resultSet = statement.executeQuery()) {
//        String passFromDb;
//        if (resultSet.next()) {
//          passFromDb = resultSet.getString(1);
//          match = password.equals(passFromDb);
//        }
//      }
//    } catch (SQLException e) {
//      throw new DaoException(e);
//    }
    return Optional.of(Role.ADMIN);
  }

  @Override
  public Optional<User> findUserByLogin(String login) throws DaoException {
    //достать юзера из бд
    User user = new User();
    user.setRole(Role.CLIENT);
    return Optional.of(user);
  }
}