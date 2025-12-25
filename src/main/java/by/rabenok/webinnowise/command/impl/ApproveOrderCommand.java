package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.service.OrderService;
import by.rabenok.webinnowise.service.impl.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ApproveOrderCommand implements Command {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    String orderId = request.getParameter(RequestParameterName.ORDER_ID);
    OrderService orderService = OrderServiceImpl.getInstance();
    String page;
    try {
      Order order = orderService.getById(Integer.parseInt(orderId));
      orderService.approve(order);
      page = PagePath.MAIN_ADMIN_JSP;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage(), e);
      request.setAttribute(RequestAttributeName.LOGIN_MSG, e.getCause());
      page = PagePath.ERROR_500;
    }
    return page;
  }
}