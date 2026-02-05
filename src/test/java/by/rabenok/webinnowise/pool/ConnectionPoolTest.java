package by.rabenok.webinnowise.pool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectionPoolTest {
  private ConnectionPool pool;

  @BeforeEach
  void setUp() throws Exception {
    resetSingleton();
    pool = ConnectionPool.getInstance();
  }

  @AfterEach
  void tearDown() throws Exception {
    pool.destroyPool();
    resetSingleton();
  }

  private void resetSingleton() throws Exception {
    Field instance = ConnectionPool.class.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, null);
  }

  @Test
  void poolShouldCreateEightConnections() {
    assertEquals(8, pool.getFreeCount());
  }

  @Test
  void getConnection_shouldReturnValidConnection() throws Exception {
    Connection connection = pool.getConnection();

    assertNotNull(connection);
    assertTrue(connection.isValid(2));
    pool.releaseConnection(connection);
  }

  @Test
  void releaseConnectionShouldReturnConnectionToFree() throws Exception {
    int countFreeStart = pool.getFreeCount();
    Connection connection = pool.getConnection();
    int countFree = pool.getFreeCount();
    pool.releaseConnection(connection);
    int countFreeEnd = pool.getFreeCount();

    assertEquals(countFreeStart, countFreeEnd);
    assertEquals(countFreeStart - 1, countFree);
  }

  @Test
  void poolShouldNotCreateNewConnectionsIfValid() throws Exception {
    Connection connection = pool.getConnection();
    pool.releaseConnection(connection);

    assertEquals(8, pool.getFreeCount());
  }

  @Test
  void poolShouldRecreateInvalidConnection() throws Exception {
    ProxyConnection proxyConnection = (ProxyConnection) pool.getConnection();
    proxyConnection.reallyClose();
    pool.releaseConnection(proxyConnection);

    assertEquals(8, pool.getFreeCount());
  }
}