package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.impl.ProcedureDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Procedure;
import by.rabenok.webinnowise.model.Rating;
import by.rabenok.webinnowise.service.ProcedureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ProcedureServiceImpl implements ProcedureService {
  private static final Logger LOGGER = LogManager.getLogger();
  private static ProcedureServiceImpl instance = new ProcedureServiceImpl();

  private ProcedureServiceImpl() {
  }

  public static ProcedureServiceImpl getInstance() {
    return instance;
  }

  @Override
  public Optional<Procedure> getProcedureByName(String name) throws ServiceException {
    if (name == null || name.isEmpty()) {
      LOGGER.error("Procedure name cannot be empty");
      throw new ServiceException("Procedure name cannot be empty");
    }
    ProcedureDaoImpl procedureDao = ProcedureDaoImpl.getInstance();
    Optional<Procedure> procedure;
    try {
      procedure = procedureDao.findProcedureByName(name);
      if (!procedure.isPresent()) {
        LOGGER.warn("Procedure with name={} not found", name);
      }
    } catch (DaoException e) {
      LOGGER.error("Error fetching procedure with name={}", name, e);
      throw new ServiceException(e);
    }
    return procedure;
  }

  @Override
  public Optional<Procedure> getProcedureById(int id) throws ServiceException {
    if (id <= 0) {
      LOGGER.error("Invalid procedure id: {} ", id);
      throw new ServiceException("Invalid procedure id: " + id);
    }
    ProcedureDaoImpl procedureDao = ProcedureDaoImpl.getInstance();
    Optional<Procedure> procedure;
    try {
      procedure = procedureDao.findProcedureById(id);
      if (!procedure.isPresent()) {
        LOGGER.warn("Procedure with id={} not found", id);
      }
    } catch (DaoException e) {
      LOGGER.error("Error fetching procedure with id={}", id, e);
      throw new ServiceException(e);
    }
    return procedure;
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
      Optional<Procedure> optionalProcedure = procedureDao.findProcedureById(procedureId);
      Procedure procedure = optionalProcedure
              .orElseThrow(() -> new ServiceException("Procedure not found."));
      procedure.addRating(rating);
      procedureDao.update(procedure);
    } catch (DaoException e) {
      LOGGER.error("Error evaluating procedure id={}", procedureId, e);
      throw new ServiceException(e);
    }
  }
}