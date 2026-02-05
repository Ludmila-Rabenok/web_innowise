package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;

public interface ProcedureDao {

  boolean addRating(int procedureId, int rating) throws DaoException;
}