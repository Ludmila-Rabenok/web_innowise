package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class RejectOrderCommand implements Command {

  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    String orderId = request.getParameter(RequestParameterName.ORDER_ID);
    String role = (String) request.getSession().getAttribute(RequestAttributeName.ROLE);
    String page;
    try {
      if (Role.ADMIN.name().equalsIgnoreCase(role)) {
        OrderServiceImpl.getInstance().reject(Integer.parseInt(orderId));
        page = PagePath.MAIN_ADMIN;
      } else
        page = PagePath.INDEX_JSP;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      throw new CommandException(e);
    }
    return page;
  }
}
