package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.impl.ProcedureDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Rating;
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
    if (procedureIdStr == null || procedureIdStr.isEmpty() || ratingStr == null || ratingStr.isEmpty()) {
      LOGGER.error("Procedure ID and rating must not be empty");
      throw new ServiceException("Procedure ID and rating must not be empty");
    }
    ProcedureDaoImpl procedureDao = ProcedureDaoImpl.getInstance();
    Rating rating;
    int procedureId;
    try {
      int ratingInt = Integer.parseInt(ratingStr);
      rating = Rating.fromValue(ratingInt);
      procedureId = Integer.parseInt(procedureIdStr);
    } catch (NumberFormatException e) {
      LOGGER.error("Invalid number format: procedureIdStr={}, ratingStr={}", procedureIdStr, ratingStr);
      throw new ServiceException(e);
    } catch (IllegalArgumentException e) {
      LOGGER.error("Invalid rating value: {}", ratingStr);
      throw new ServiceException(e);
    }
    try {
      procedureDao.addRating(procedureId, rating);
    } catch (DaoException e) {
      LOGGER.error("Error evaluating procedure id={}", procedureId, e);
      throw new ServiceException(e);
    }
  }
}