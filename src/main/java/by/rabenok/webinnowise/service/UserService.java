package by.rabenok.webinnowise.service;

import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;

import java.util.Optional;

public interface UserService {
  boolean authenticate(String login, String password) throws ServiceException;

  Optional<Role> authorize(String login) throws ServiceException;
}