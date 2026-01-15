package by.rabenok.webinnowise.service;

import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;

import java.util.Optional;

public interface UserService {
  boolean authenticate(String login, String password) throws ServiceException;

  Optional<Role> authorize(String login) throws ServiceException;

  User findUserByName(String userName) throws ServiceException;

  User findUserById(int id) throws ServiceException;
}