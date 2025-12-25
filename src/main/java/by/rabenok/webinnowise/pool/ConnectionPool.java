package by.rabenok.webinnowise.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
  private static ConnectionPool instance;
  private static final String DRIVER = "org.postgresql.Driver";
  private static final int CAPACITY = 8;
  private static final Lock lock = new ReentrantLock();
  private BlockingQueue<ProxyConnection> free = new LinkedBlockingQueue<>(CAPACITY);
  private BlockingQueue<ProxyConnection> used = new LinkedBlockingQueue<>(CAPACITY);

  static {
    try {
      Class.forName(DRIVER);
    } catch (ClassNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private ConnectionPool() {
    String url = "jdbc:postgresql://localhost:5432/salon";
    String username = "postgres";
    String password = "963Luda963";
    for (int i = 0; i < CAPACITY; i++) {
      ProxyConnection connection = createConnection(url, username, password);
      free.add(connection);
    }
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


  public Connection getConnection() {
    ProxyConnection connection = null;
    try {
      connection = free.take();
      used.put(connection);
    } catch (InterruptedException e) {
      //log
      Thread.currentThread().interrupt();
    }
    return connection;
  }

  public boolean releaseConnection(Connection connection) {
    boolean match = true;
    try {
      match = used.remove(connection);
      if (match) {
        free.put((ProxyConnection) connection);
      }
    } catch (InterruptedException e) {
      //log
      Thread.currentThread().interrupt();
    }
    return match;
  }

  public void destroyPool() {
    for (int i = 0; i < CAPACITY; i++) {
      try {
        free.take().reallyClose();
      } catch (InterruptedException e) {
        //log e.printSackTrace();
      }
    }
    DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
      try {
        DriverManager.deregisterDriver(driver);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  private ProxyConnection createConnection(String url, String username, String password) {
    ProxyConnection proxyConnection;
    try {
      proxyConnection = new ProxyConnection(DriverManager.getConnection(url, username, password), instance);
    } catch (SQLException e) {
      //log
      throw new ExceptionInInitializerError(e);
    }
    return proxyConnection;
  }
}
