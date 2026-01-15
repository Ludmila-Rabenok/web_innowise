package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.ConstantSql;
import by.rabenok.webinnowise.dao.ProcedureDao;
import by.rabenok.webinnowise.exception.ConnectionException;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Rating;
import by.rabenok.webinnowise.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProcedureDaoImpl implements ProcedureDao {
  private static ProcedureDaoImpl instance = new ProcedureDaoImpl();

  private ProcedureDaoImpl() {
  }

  public static ProcedureDaoImpl getInstance() {
    return instance;
  }

  @Override
  public void addRating(int procedureId, Rating rating) throws DaoException {
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = connection.prepareStatement(ConstantSql.INSERT_PROCEDURE_RATING)) {
      ps.setInt(1, procedureId);
      ps.setString(2, rating.name());
      ps.executeUpdate();
    } catch (SQLException | ConnectionException e) {
      throw new DaoException(e);
    }
  }
}