package by;

import by.rabenok.webinnowise.dao.UserDao;
import by.rabenok.webinnowise.dao.impl.UserDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;
import by.rabenok.webinnowise.service.UserService;
import by.rabenok.webinnowise.util.PasswordBCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = LogManager.getLogger();
  private static UserServiceImpl instance = new UserServiceImpl();

  private UserServiceImpl() {
  }

  public static UserServiceImpl getInstance() {
    return instance;
  }

  @Override
  public boolean authenticate(String login, String password) throws ServiceException {
    UserDao userDao = UserDaoImpl.getInstance();
    String passFromDb;
    try {
      passFromDb = userDao.authenticate(login);
      if (passFromDb == null) {
        return false;
      }
      return PasswordBCrypt.verifyPassword(password, passFromDb);
    } catch (DaoException e) {
      LOGGER.error("Error during authentication for user {}", login, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<Role> authorize(String login) throws ServiceException {
    Optional<Role> optionalRole;
    try {
      optionalRole = UserDaoImpl.getInstance().authorize(login);
    } catch (DaoException e) {
      LOGGER.error("Error authorize user with login={}", login, e);
      throw new ServiceException(e);
    }
    return optionalRole;
  }

  @Override
  public User findUserByName(String userName) throws ServiceException {
    User user;
    try {
      Optional<User> optionalUser = UserDaoImpl.getInstance().findUserByName(userName);
      user = optionalUser.orElseThrow(() -> new ServiceException("User not found."));
    } catch (DaoException e) {
      LOGGER.error("Error fetching user with name ={}", userName, e);
      throw new ServiceException(e);
    }
    return user;
  }

  @Override
  public User findUserById(int id) throws ServiceException {
    User user;
    try {
      Optional<User> optionalUser = UserDaoImpl.getInstance().findUserById(id);
      user = optionalUser.orElseThrow(() -> new ServiceException("User not found."));
    } catch (DaoException e) {
      LOGGER.error("Error fetching user with id={}", id, e);
      throw new ServiceException(e);
    }
    return user;
  }
}