package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;

import java.util.Optional;

public interface UserDao {
  Optional<Role> authenticate(String login, String password) throws DaoException;

  Optional<User> findUserByLogin(String login) throws DaoException;
}
