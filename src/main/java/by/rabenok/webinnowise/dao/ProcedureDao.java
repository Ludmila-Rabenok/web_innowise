package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;

public interface ProcedureDao {

  void addRating(int procedureId, int rating) throws DaoException;
}