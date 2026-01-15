package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.service.impl.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class NewOrderCommand implements Command {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    String[] procedures = request.getParameterValues(RequestParameterName.PROCEDURES);
    String date = request.getParameter(RequestParameterName.DATE);
    String time = request.getParameter(RequestParameterName.TIME);
    String userName = (String) request.getSession().getAttribute(RequestAttributeName.USER);
    String page;
    try {
      OrderServiceImpl.getInstance().createOrder(userName, procedures, date, time);
      page = PagePath.SUCCESS_ORDERS;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      request.setAttribute(RequestAttributeName.LOGIN_MSG, e.getCause());
      page = PagePath.ERROR_500;
    }
    return page;
  }
}