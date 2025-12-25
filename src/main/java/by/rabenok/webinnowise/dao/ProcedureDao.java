package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Procedure;
import by.rabenok.webinnowise.model.Rating;

import java.util.Optional;

public interface ProcedureDao {
  Optional<Procedure> findProcedureByName(String name) throws DaoException;

  Optional<Procedure> findProcedureById(int id) throws DaoException;

  void update(Procedure procedure) throws DaoException;
}
