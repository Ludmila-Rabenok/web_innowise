package by.rabenok.webinnowise.dao;

public final class ConstantSql {
  public static final String SELECT_LOGIN_PASSWORD = "SELECT password FROM users WHERE lastname = ?";

  private ConstantSql() {
  }
}
