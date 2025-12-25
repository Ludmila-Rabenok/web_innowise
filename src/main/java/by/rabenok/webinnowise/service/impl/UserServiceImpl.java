package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.UserDao;
import by.rabenok.webinnowise.dao.impl.UserDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;
import by.rabenok.webinnowise.service.UserService;
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
  public Optional<Role> authenticate(String login, String password) throws ServiceException {
    if (login == null || login.isEmpty()) {
      LOGGER.error("Login cannot be empty");
      throw new ServiceException("Login cannot be empty");
    }
    UserDao userDao = UserDaoImpl.getInstance();
    Optional<Role> role;
    try {
      role = userDao.authenticate(login, password);
    } catch (DaoException e) {
      LOGGER.error("Error during authentication for user {}", login, e);
      throw new ServiceException(e);
    }
    return role;
  }

  @Override
  public User getUserByLogin(String login) throws ServiceException {
    if (login == null || login.isEmpty()) {
      LOGGER.error("Login cannot be empty");
      throw new ServiceException("Login cannot be empty");
    }
    UserDao userDao = UserDaoImpl.getInstance();
    User user;
    try {
      Optional<User> optionalUser = userDao.findUserByLogin(login);
      user = optionalUser.orElseThrow(() -> new ServiceException("User not found."));
    } catch (DaoException e) {
      LOGGER.error("Error fetching user with login={}", login, e);
      throw new ServiceException(e);
    }
    return user;
  }
}