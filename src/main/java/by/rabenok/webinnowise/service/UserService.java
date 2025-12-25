package by.rabenok.webinnowise.service;

import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;

import java.util.Optional;

public interface UserService {
  Optional<Role> authenticate(String login, String password) throws ServiceException;

  User getUserByLogin(String login) throws ServiceException;

}
