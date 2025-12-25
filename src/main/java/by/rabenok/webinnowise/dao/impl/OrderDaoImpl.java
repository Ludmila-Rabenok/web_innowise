package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.dao.OrderDao;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Procedure;
import by.rabenok.webinnowise.model.Status;
import by.rabenok.webinnowise.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
  private static OrderDaoImpl instance = new OrderDaoImpl();

  private OrderDaoImpl() {
  }

  public static OrderDaoImpl getInstance() {
    return instance;
  }

  public List<Order> findOrdersFromUser(User user) throws DaoException {
    //доставть все заявки, для этого юзера
    Procedure procedure = new Procedure("Стрижка", new BigDecimal(5));
    List<Procedure> procedures = new ArrayList<>();
    procedures.add(procedure);
    Order order = new Order();
    order.setStatus(Status.MODERATION);
    order.setId(1);
    order.setLeadTime(LocalDateTime.now());
    order.setProcedures(procedures);
    List<Order> orders = new ArrayList<>();
    orders.add(order);
    return orders;
  }

  @Override
  public void save(Order order) throws DaoException {
  }
  @Override
  public void update(Order order) throws DaoException {
  }

  @Override
  public List<Order> findAll() throws DaoException {
    List<Order> orders = new ArrayList<>();
    Order order = new Order();
    order.setStatus(Status.MODERATION);
    order.setId(1);
    Procedure procedure = new Procedure("мытье", new BigDecimal(2));
    Procedure procedure2 = new Procedure("стрижка", new BigDecimal(8));
    List<Procedure> procedures = new ArrayList<>();
    procedures.add(procedure);
    procedures.add(procedure2);
    order.setProcedures(procedures);
    order.setLeadTime(LocalDateTime.now());
    orders.add(order);
    return orders;
  }

  @Override
  public Optional<Order> findById(int id) throws DaoException {
    Order order = new Order();
    order.setStatus(Status.MODERATION);
    order.setId(1);
    Procedure procedure = new Procedure("мытье", new BigDecimal(2));
    Procedure procedure2 = new Procedure("стрижка", new BigDecimal(8));
    List<Procedure> procedures = new ArrayList<>();
    procedures.add(procedure);
    procedures.add(procedure2);
    order.setProcedures(procedures);
    order.setLeadTime(LocalDateTime.now());
    return Optional.of(order);
  }
}