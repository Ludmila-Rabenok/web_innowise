package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.service.ProcedureService;
import by.rabenok.webinnowise.service.impl.ProcedureServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class EvaluationProcedureCommand implements Command {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    String procedureId = request.getParameter(RequestParameterName.PROCEDURE_ID);
    String ratingStr = request.getParameter(RequestParameterName.RATING);
    ProcedureService procedureService = ProcedureServiceImpl.getInstance();
    String page;
    try {
      procedureService.evaluate(procedureId, ratingStr);
      page = PagePath.RATING_PROCEDURE;
    } catch (ServiceException e) {
      LOGGER.error(e.getMessage(), e);
      request.setAttribute(RequestAttributeName.LOGIN_MSG, e.getCause());
      page = PagePath.ERROR_500;
    }
    return page;
  }
}