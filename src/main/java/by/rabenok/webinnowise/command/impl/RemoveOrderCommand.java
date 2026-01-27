package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class RemoveOrderCommand implements Command {
  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    String orderId = request.getParameter(RequestParameterName.ORDER_ID);
    String page;
    try {
      boolean remove = OrderServiceImpl.getInstance().remove(Integer.parseInt(orderId));
      if (remove) {
        request.setAttribute(RequestAttributeName.MSG, "Вы успешно отменили заявку.");
      } else {
        request.setAttribute(RequestAttributeName.MSG, "Такой заявки не существует. Удаление невозможно.");
      }
      page = PagePath.ORDER_REMOVE_RESULT;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      throw new CommandException(e);
    }
    return page;
  }
}
