package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.service.UserService;
import by.rabenok.webinnowise.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class LoginCommand implements Command {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    String login = request.getParameter(RequestParameterName.LOGIN);
    String password = request.getParameter(RequestParameterName.PASS);
    UserService userService = UserServiceImpl.getInstance();
    String page = "";
    try {
      if (userService.authenticate(login, password)) {
        Optional<Role> optionalRole = userService.authorize(login);
        if (optionalRole.isPresent()) {
          HttpSession session = request.getSession();
          session.setAttribute(RequestAttributeName.USER, login);
          Role role = optionalRole.get();
          if (role == Role.CLIENT) {
            page = PagePath.MAIN_CLIENT_JSP;
          } else if (role == Role.ADMIN) {
            ;
            page = PagePath.MAIN_ADMIN_JSP;
          }
          session.setAttribute(RequestAttributeName.ROLE, role.name());
        } else {
          request.setAttribute(RequestAttributeName.LOGIN_MSG, "Authorization error");
          page = PagePath.INDEX_JSP;
        }
      }
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      request.setAttribute(RequestAttributeName.LOGIN_MSG, e.getCause());
      page = PagePath.ERROR_500;
    }
    return page;
  }
}