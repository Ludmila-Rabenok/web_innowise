package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.service.OrderService;
import by.rabenok.webinnowise.service.impl.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AdminOrdersCommand implements Command {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    OrderService orderService = OrderServiceImpl.getInstance();
    String role = (String) request.getSession().getAttribute(RequestAttributeName.ROLE);
    String page;
    try {
      if (Role.ADMIN.name().equalsIgnoreCase(role)) {
        List<Order> orders = orderService.findAll();
        request.setAttribute(RequestAttributeName.ORDERS, orders);
        page = PagePath.ADMIN_ORDERS;
      } else page = PagePath.INDEX_JSP;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      request.setAttribute(RequestAttributeName.LOGIN_MSG, e.getCause());
      page = PagePath.ERROR_500;
    }
    return page;
  }
}