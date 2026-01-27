package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.service.OrderService;
import by.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ClientOrdersCommand implements Command {

  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    String login = request.getSession().getAttribute(RequestAttributeName.USER).toString();
    OrderService orderService = OrderServiceImpl.getInstance();
    String page;
    try {
      List<Order> orderList = orderService.findOrdersByUserName(login);
      request.setAttribute(RequestAttributeName.ORDERS, orderList);
      page = PagePath.CLIENT_ORDERS;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      throw new CommandException(e);
    }
    return page;
  }
}