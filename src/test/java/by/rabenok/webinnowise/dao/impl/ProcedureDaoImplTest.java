package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.pool.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcedureDaoImplTest {

  @BeforeEach
  void resetTable() throws Exception {
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      Path path = Paths.get("src/test/resources/reset_db.sql");
      String sql = new String(Files.readAllBytes(path));
      statement.execute(sql);
    }
  }

  @Test
  void addRating_shouldUpdateProcedureAndReturnTrue() throws Exception {
    boolean actual = ProcedureDaoImpl.getInstance().addRating(1, 5);

    assertTrue(actual);
  }

  @Test
  void addRating_shouldReturnFalse_whenProcedureNotFound() throws Exception {
    boolean actual = ProcedureDaoImpl.getInstance().addRating(999, 5);

    assertFalse(actual);
  }

  @Test
  void addRating_shouldThrowDaoException_whenRatingInvalid() {
    assertThrows(DaoException.class, () -> ProcedureDaoImpl.getInstance().addRating(1, 10));
  }
}