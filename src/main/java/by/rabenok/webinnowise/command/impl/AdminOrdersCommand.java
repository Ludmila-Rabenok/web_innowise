package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AdminOrdersCommand implements Command {

  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    String role = (String) request.getSession().getAttribute(RequestAttributeName.ROLE);
    String page;
    try {
      if (Role.ADMIN.name().equalsIgnoreCase(role)) {
        List<Order> orders = OrderServiceImpl.getInstance().findAll();
        request.setAttribute(RequestAttributeName.ORDERS, orders);
        page = PagePath.ADMIN_ORDERS;
      } else page = PagePath.INDEX_JSP;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      throw new CommandException(e);
    }
    return page;
  }
}