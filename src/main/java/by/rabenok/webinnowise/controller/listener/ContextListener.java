package by.rabenok.webinnowise.controller.listener;

import by.rabenok.webinnowise.exception.ConnectionException;
import by.rabenok.webinnowise.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    try {
      ConnectionPool.getInstance().destroyPool();
    } catch (ConnectionException e) {
      LOGGER.error("Failed to destroy connection pool", e);
    }
  }
}