package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class NewOrderCommand implements Command {

  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    String[] procedures = request.getParameterValues(RequestParameterName.PROCEDURES);
    String date = request.getParameter(RequestParameterName.DATE);
    String time = request.getParameter(RequestParameterName.TIME);
    String userName = (String) request.getSession().getAttribute(RequestAttributeName.USER);
    if (userName == null || userName.isEmpty()) {
      LOGGER.warn("User is not logged in");
      request.setAttribute(RequestAttributeName.LOGIN_MSG, "Authentication error");
      return PagePath.INDEX_JSP;
    }
    if (procedures == null || procedures.length == 0) {
      LOGGER.warn("At least one procedure required");
      request.setAttribute(RequestAttributeName.ORDER_ERROR, "At least one procedure required");
      return PagePath.MAIN_CLIENT;
    }
    if (date == null || date.isEmpty() || time == null || time.isEmpty()) {
      LOGGER.warn("Enter date and time");
      request.setAttribute(RequestAttributeName.ORDER_ERROR, "Enter date and time");
      return PagePath.MAIN_CLIENT;
    }
    try {
      OrderServiceImpl.getInstance().createOrder(userName, procedures, date, time);
      return "redirect:/controller?command=success_order";
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      throw new CommandException(e);
    }
  }
}