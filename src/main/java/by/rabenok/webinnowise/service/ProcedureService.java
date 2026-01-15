package by.rabenok.webinnowise.service;

import by.rabenok.webinnowise.exception.ServiceException;

public interface ProcedureService {

  void evaluate(String procedureId, String ratingStr) throws ServiceException;
}