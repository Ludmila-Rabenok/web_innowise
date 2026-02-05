package by.rabenok.webinnowise.pool;

import by.rabenok.webinnowise.exception.ConnectionException;
import by.rabenok.webinnowise.util.DbConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
  public static final Logger LOGGER = LogManager.getLogger();
  private static final int CAPACITY = 8;
  private static ConnectionPool instance;
  private static Lock lock = new ReentrantLock();
  private BlockingQueue<ProxyConnection> free = new ArrayBlockingQueue<>(CAPACITY);
  private BlockingQueue<ProxyConnection> used = new ArrayBlockingQueue<>(CAPACITY);

  static {
    try {
      Class.forName(DbConfig.getDriver());
    } catch (ClassNotFoundException e) {
      LOGGER.fatal("Failed to load JDBC driver", e);
      throw new ExceptionInInitializerError(e);
    }
  }

  private ConnectionPool() {
    for (int i = 0; i < CAPACITY; i++) {
      ProxyConnection connection = createConnection(
              DbConfig.getUrl(),
              DbConfig.getUsername(),
              DbConfig.getPassword());
      free.add(connection);
    }
  }

  public int getFreeCount() {
    return free.size();
  }

  public static ConnectionPool getInstance() {
    if (instance == null) {
      lock.lock();
      try {
        if (instance == null) {
          instance = new ConnectionPool();
        }
      } finally {
        lock.unlock();
      }
    }
    return instance;
  }


  public Connection getConnection() throws ConnectionException {
    ProxyConnection connection = null;
    try {
      connection = free.take();
      if (!connection.isValid(2)) {
        LOGGER.warn("Connection invalid, recreating...");
        connection.reallyClose();
        connection = createConnection(
                DbConfig.getUrl(),
                DbConfig.getUsername(),
                DbConfig.getPassword());
      }
      used.put(connection);
    } catch (InterruptedException e) {
      LOGGER.error("Thread interrupted while getting connection", e);
      Thread.currentThread().interrupt();
    } catch (SQLException e) {
      LOGGER.error("Connection validation failed", e);
      throw new ConnectionException(e);
    }
    return connection;
  }

  public boolean releaseConnection(Connection connection) {
    if (!(connection instanceof ProxyConnection)) {
      LOGGER.warn("Attempted to release invalid connection: {}", connection);
      return false;
    }
    boolean match = true;
    try {
      match = used.remove(connection);
      if (match) {
        if (connection.isValid(2)) {
          free.put((ProxyConnection) connection);
        } else {
          LOGGER.warn("Connection {} invalid on release, recreating...", connection);
          ((ProxyConnection) connection).reallyClose();
          free.put(createConnection(
                  DbConfig.getUrl(),
                  DbConfig.getUsername(),
                  DbConfig.getPassword()));
        }
      }
    } catch (InterruptedException e) {
      LOGGER.error("Thread interrupted while releasing connection", e);
      Thread.currentThread().interrupt();
    } catch (SQLException e) {
      LOGGER.error("Connection release failed", e);
      throw new RuntimeException(e);
    }
    return match;
  }

  public void destroyPool() throws ConnectionException {
    while (!free.isEmpty()) {
      for (int i = 0; i < CAPACITY; i++) {
        try {
          ProxyConnection connection = free.take();
          if (!connection.isClosed()) {
            connection.reallyClose();
          }
        } catch (InterruptedException e) {
          LOGGER.error("Thread interrupted while destroying pool", e);
        } catch (SQLException e) {
          LOGGER.error("Error closing connection during pool destroy", e);
        }
      }
    }
  }

  private ProxyConnection createConnection(String url, String username, String password) {
    ProxyConnection proxyConnection;
    try {
      proxyConnection = new ProxyConnection(DriverManager.getConnection(url, username, password));
    } catch (SQLException e) {
      LOGGER.error("Failed to create connection", e);
      throw new ExceptionInInitializerError(e);
    }
    return proxyConnection;
  }
}