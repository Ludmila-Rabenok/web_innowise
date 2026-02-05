package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.service.impl.ProcedureServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class EvaluationProcedureCommand implements Command {

  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    String procedureIdStr = request.getParameter(RequestParameterName.PROCEDURE_ID);
    String ratingStr = request.getParameter(RequestParameterName.RATING);
    try {
      ProcedureServiceImpl.getInstance().evaluate(procedureIdStr, ratingStr);
      return PagePath.RATING_PROCEDURE;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage());
      throw new CommandException(e);
    }
  }
}