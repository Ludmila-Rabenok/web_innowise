package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.ProcedureDao;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Procedure;

import java.math.BigDecimal;
import java.util.Optional;

public class ProcedureDaoImpl implements ProcedureDao {
  private static ProcedureDaoImpl instance = new ProcedureDaoImpl();

  private ProcedureDaoImpl() {
  }

  public static ProcedureDaoImpl getInstance() {
    return instance;
  }

  @Override
  public Optional<Procedure> findProcedureByName(String name) throws DaoException {
    //достаем из бд процедуру по имени
    Procedure procedure = new Procedure("стрижка", new BigDecimal(55));//временно
    return Optional.of(procedure);
  }

  @Override
  public Optional<Procedure> findProcedureById(int id) throws DaoException {
    Procedure procedure = new Procedure("", new BigDecimal(5));
    return Optional.of(procedure);
  }

  @Override
  public void update(Procedure procedure) throws DaoException {
  }
}
