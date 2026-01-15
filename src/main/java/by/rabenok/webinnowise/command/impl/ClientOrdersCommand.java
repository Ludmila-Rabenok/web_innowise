package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.service.OrderService;
import by.rabenok.webinnowise.service.impl.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ClientOrdersCommand implements Command {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    String login = request.getSession().getAttribute(RequestAttributeName.USER).toString();
    OrderService orderService = OrderServiceImpl.getInstance();
    String page;
    try {
      List<Order> orderList = orderService.findOrdersByUserName(login);
      request.setAttribute(RequestAttributeName.ORDERS, orderList);
      page = PagePath.CLIENT_ORDERS;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      request.setAttribute(RequestAttributeName.LOGIN_MSG, e.getCause());
      page = PagePath.ERROR_500;
    }
    return page;
  }
}