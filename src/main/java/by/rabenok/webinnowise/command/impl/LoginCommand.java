package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.service.UserService;
import by.rabenok.webinnowise.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class LoginCommand implements Command {

  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    String login = request.getParameter(RequestParameterName.LOGIN);
    String password = request.getParameter(RequestParameterName.PASS);
    String page = PagePath.INDEX_JSP;
    if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
      LOGGER.warn("Login and password cannot be empty");
      request.setAttribute(RequestAttributeName.LOGIN_MSG, "Enter login and password");
      return page;
    }
    UserService userService = UserServiceImpl.getInstance();
    try {
      if (userService.authenticate(login, password)) {
        Optional<Role> optionalRole = userService.authorize(login);
        if (optionalRole.isPresent()) {
          HttpSession session = request.getSession();
          session.setAttribute(RequestAttributeName.USER, login);
          Role role = optionalRole.get();
          if (role == Role.CLIENT) {
            page = PagePath.MAIN_CLIENT;
          } else if (role == Role.ADMIN) {
            page = PagePath.MAIN_ADMIN;
          }
          session.setAttribute(RequestAttributeName.ROLE, role.name());
        }
      } else {
        LOGGER.warn("Incorrect login or password");
        request.setAttribute(RequestAttributeName.LOGIN_MSG, "Authentication error");
        page = PagePath.INDEX_JSP;
      }
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      throw new CommandException(e);
    }
    return page;
  }
}