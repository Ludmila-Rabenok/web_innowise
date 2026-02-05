package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.impl.UserDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
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
    try {
      String passFromDb = UserDaoImpl.getInstance().authenticate(login);
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
}