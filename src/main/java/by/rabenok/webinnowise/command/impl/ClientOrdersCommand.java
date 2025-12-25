package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.User;
import by.rabenok.webinnowise.service.OrderService;
import by.rabenok.webinnowise.service.UserService;
import by.rabenok.webinnowise.service.impl.OrderServiceImpl;
import by.rabenok.webinnowise.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ClientOrdersCommand implements Command {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    String login = request.getSession().getAttribute(RequestAttributeName.USER).toString();
    UserService userService = UserServiceImpl.getInstance();
    OrderService orderService = OrderServiceImpl.getInstance();
    String page;
    try {
      User user = userService.getUserByLogin(login);
      List<Order> orderList = orderService.getOrdersFromUser(user);
      request.setAttribute(RequestAttributeName.ORDERS, orderList);
      page = PagePath.CLIENT_ORDERS;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage(), e);
      request.setAttribute(RequestAttributeName.LOGIN_MSG, e.getCause());
      page = PagePath.ERROR_500;
    }
    return page;
  }
}