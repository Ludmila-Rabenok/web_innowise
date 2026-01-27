package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;

import java.util.Optional;

public interface UserDao {
  String authenticate(String login) throws DaoException;

  Optional<Role> authorize(String login) throws DaoException;

  Optional<User> findUserByName(String UserName) throws DaoException;

  Optional<User> findUserById(int id) throws DaoException;
}