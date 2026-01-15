package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Rating;

public interface ProcedureDao {

  void addRating(int procedureId, Rating rating) throws DaoException;
}