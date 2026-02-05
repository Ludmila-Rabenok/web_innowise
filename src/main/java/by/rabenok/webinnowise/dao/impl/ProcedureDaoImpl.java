package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.ConstantSql;
import by.rabenok.webinnowise.dao.ProcedureDao;
import by.rabenok.webinnowise.exception.ConnectionException;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProcedureDaoImpl implements ProcedureDao {
  private static ProcedureDaoImpl instance = new ProcedureDaoImpl();
  public static final Logger LOGGER = LogManager.getLogger();

  private ProcedureDaoImpl() {
  }

  public static ProcedureDaoImpl getInstance() {
    return instance;
  }

  @Override
  public boolean addRating(int procedureId, int rating) throws DaoException {
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = connection.prepareStatement(ConstantSql.INSERT_RATING_PROCEDURE)) {
      ps.setDouble(1, rating);
      ps.setDouble(2, rating);
      ps.setInt(3, procedureId);
      int countUpdatedLines = ps.executeUpdate();
      return countUpdatedLines == 1;
    } catch (SQLException | ConnectionException e) {
      LOGGER.error("Error when changing rating" + e);
      throw new DaoException(e);
    }
  }
}