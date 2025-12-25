package by.rabenok.webinnowise.service;

import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Procedure;

import java.util.Optional;

public interface ProcedureService {
  Optional<Procedure> getProcedureByName(String name) throws ServiceException;

  Optional<Procedure> getProcedureById(int id) throws ServiceException;

  void evaluate(String procedureId, String ratingStr) throws ServiceException;

}
