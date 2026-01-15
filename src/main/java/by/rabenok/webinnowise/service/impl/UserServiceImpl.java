package by.rabenok.webinnowise.service.impl;

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
    if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
      LOGGER.error("Login and password cannot be empty");
      throw new ServiceException("Login and password cannot be empty");
    }
    UserDao userDao = UserDaoImpl.getInstance();
    String passFromDb;
    String hashPassword = PasswordBCrypt.hashPassword(password);
    try {
      passFromDb = userDao.authenticate(login, hashPassword);
      if (PasswordBCrypt.verifyPassword(password, passFromDb)) {
        return true;
      } else {
        LOGGER.error("Password incorrect");
        throw new ServiceException("Password incorrect");
      }
    } catch (DaoException e) {
      LOGGER.error("Error during authentication for user {}", login, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<Role> authorize(String login) throws ServiceException {
    if (login == null || login.isEmpty()) {
      throw new ServiceException("Login cannot be empty");
    }
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
    if (userName == null || userName.isEmpty()) {
      LOGGER.error("Login cannot be empty");
      throw new ServiceException("Login cannot be empty");
    }
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
    if (id <= 0) {
      LOGGER.error("ID cannot be less than zero");
      throw new ServiceException("ID cannot be less than zero");
    }
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