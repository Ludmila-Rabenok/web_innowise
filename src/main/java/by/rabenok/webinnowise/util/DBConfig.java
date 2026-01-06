package by.rabenok.webinnowise.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {
  public static final Logger LOGGER = LogManager.getLogger();
  private static final Properties properties = new Properties();

  static {
    try (InputStream input = DBConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
      if (input == null) {
        LOGGER.error("db.properties not found");
        throw new RuntimeException("db.properties not found");
      }
      properties.load(input);
    } catch (IOException e) {
      LOGGER.error("Failed to load db.properties");
      throw new RuntimeException("Failed to load db.properties", e);
    }
  }

  public static String getUrl() {
    return properties.getProperty("db.url");
  }

  public static String getUsername() {
    return properties.getProperty("db.username");
  }

  public static String getPassword() {
    return properties.getProperty("db.password");
  }

  public static String getDriver() {
    return properties.getProperty("db.driver");
  }
}