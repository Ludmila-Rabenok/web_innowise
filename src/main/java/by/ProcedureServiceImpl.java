package by;

import by.rabenok.webinnowise.dao.impl.ProcedureDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.service.ProcedureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcedureServiceImpl implements ProcedureService {
  private static final Logger LOGGER = LogManager.getLogger();
  private static ProcedureServiceImpl instance = new ProcedureServiceImpl();

  private ProcedureServiceImpl() {
  }

  public static ProcedureServiceImpl getInstance() {
    return instance;
  }

  @Override
  public void evaluate(String procedureIdStr, String ratingStr) throws ServiceException {
    int procedureId;
    int rating;
    try {
      rating = Integer.parseInt(ratingStr);
      procedureId = Integer.parseInt(procedureIdStr);
    } catch (NumberFormatException e) {
      LOGGER.error("Invalid number format: procedureIdStr={}, ratingStr={}", procedureIdStr, ratingStr);
      throw new ServiceException(e);
    }
    try {
      ProcedureDaoImpl.getInstance().addRating(procedureId, rating);
    } catch (DaoException e) {
      LOGGER.error("Error evaluating procedure id={}", procedureId, e);
      throw new ServiceException(e);
    }
  }
}