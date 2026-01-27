package by.rabenok.webinnowise.dao;

public final class ConstantSql {
  public static final String SELECT_ORDERS_BY_USERNAME = "SELECT o.id AS order_id, o.lead_time AS order_lead_time, o.status AS order_status, o.bill AS order_bill, u.id AS user_id, u.name AS user_name, u.role AS user_role, p.id AS procedure_id, p.name AS procedure_name, p.price AS procedure_price, p.rating_average, p.rating_count FROM orders o JOIN users u ON o.user_id = u.id LEFT JOIN order_procedure op ON o.id = op.order_id LEFT JOIN procedures p ON op.procedure_id = p.id WHERE u.name = ?";
  public static final String INSERT_RATING_PROCEDURE = "UPDATE procedures SET rating_average = CASE WHEN rating_average IS NULL THEN ? ELSE (rating_average * rating_count + ?) / (rating_count + 1) END, rating_count = rating_count + 1 WHERE id = ?";
  public static final String SELECT_PASSWORD_BY_USERNAME = "SELECT password FROM users WHERE name = ?";
  public static final String SELECT_ROLE_BY_USERNAME = "SELECT role FROM users WHERE name = ?";
  public static final String SELECT_USER_BY_USERNAME = "SELECT id, role FROM users WHERE name = ?";
  public static final String SELECT_USER_BY_ID = "SELECT name, role FROM users WHERE id = ?";
  public static final String INSERT_ORDERS = "INSERT INTO orders (user_id, lead_time) VALUES (?, ?) RETURNING id";
  public static final String INSERT_ORDER_PROCEDURE = "INSERT INTO order_procedure (order_id, procedure_id) VALUES (?, (SELECT id FROM procedures WHERE name = ?))";
  public static final String UPDATE_ORDER = "UPDATE orders SET status = ?::status_enum, bill = ? WHERE id = ?";
  public static final String SELECT_ORDERS = "SELECT o.id AS order_id, o.lead_time AS order_lead_time, o.status AS order_status, o.bill AS order_bill, u.id AS user_id, u.name AS user_name, u.role AS user_role, p.id AS procedure_id, p.name AS procedure_name, p.price AS procedure_price, p.rating_average, p.rating_count FROM orders o JOIN users u ON o.user_id = u.id LEFT JOIN order_procedure op ON o.id = op.order_id LEFT JOIN procedures p ON op.procedure_id = p.id";
  public static final String SELECT_ORDERS_BY_ID = "SELECT o.id AS order_id, o.lead_time AS order_lead_time, o.status AS order_status, o.bill AS order_bill, u.id AS user_id, u.name AS user_name, u.role AS user_role, p.id AS procedure_id, p.name AS procedure_name, p.price AS procedure_price, p.rating_average, p.rating_count FROM orders o JOIN users u ON o.user_id = u.id LEFT JOIN order_procedure op ON o.id = op.order_id LEFT JOIN procedures p ON op.procedure_id = p.id WHERE o.id = ?";

  public static final String DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE id = ?";
  private ConstantSql() {
  }
}