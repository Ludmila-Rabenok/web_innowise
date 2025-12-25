package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.OrderDao;
import by.rabenok.webinnowise.dao.ProcedureDao;
import by.rabenok.webinnowise.dao.UserDao;
import by.rabenok.webinnowise.dao.impl.OrderDaoImpl;
import by.rabenok.webinnowise.dao.impl.ProcedureDaoImpl;
import by.rabenok.webinnowise.dao.impl.UserDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Procedure;
import by.rabenok.webinnowise.model.Status;
import by.rabenok.webinnowise.model.User;
import by.rabenok.webinnowise.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
  private static final Logger LOGGER = LogManager.getLogger();
  private static OrderServiceImpl instance = new OrderServiceImpl();

  private OrderServiceImpl() {
  }

  public static OrderServiceImpl getInstance() {
    return instance;
  }

  @Override
  public void createOrder(String login, String[] procedures, String date, String time) throws ServiceException {
    if (login == null || login.isEmpty()) {
      LOGGER.error("Login cannot be empty");
      throw new ServiceException("Login cannot be empty");
    }
    if (procedures == null || procedures.length == 0) {
      LOGGER.error("At least one procedure required");
      throw new ServiceException("At least one procedure required");
    }
    Order order = new Order();
    UserDao userDao = UserDaoImpl.getInstance();
    User user;
    try {
      user = userDao.findUserByLogin(login)
              .orElseThrow(() -> new ServiceException("User not found"));
      order.setUser(user);
      LocalDate localDate = LocalDate.parse(date);
      LocalTime localTime = LocalTime.parse(time);
      LocalDateTime leadTime = LocalDateTime.of(localDate, localTime);
      order.setLeadTime(leadTime);
      List<Procedure> procedureList = new ArrayList<>();
      ProcedureDao procedureDao = ProcedureDaoImpl.getInstance();
      for (String nameProc : procedures) {
        Optional<Procedure> procedureOptional = procedureDao.findProcedureByName(nameProc);
        procedureOptional.ifPresent(procedureList::add);
      }
      if (procedureList.isEmpty()) {
        LOGGER.error("No valid procedures found");
        throw new ServiceException("No valid procedures found");
      }
      order.setProcedures(procedureList);
      order.setStatus(Status.MODERATION);
      OrderDao orderDao = OrderDaoImpl.getInstance();
      orderDao.save(order);
    } catch (DaoException e) {
      LOGGER.error("Error creating order", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Order> getOrdersFromUser(User user) throws ServiceException {
    if (user == null || user.getId() <= 0) {
      LOGGER.error("Invalid user");
      throw new ServiceException("Invalid user");
    }
    OrderDao orderDao = OrderDaoImpl.getInstance();
    List<Order> orders;
    try {
      orders = orderDao.findOrdersFromUser(user);
      if (orders == null) {
        orders = Collections.emptyList();
      }
    } catch (DaoException e) {
      LOGGER.error("Error fetching orders for user id={}", user.getId(), e);
      throw new ServiceException(e);
    }
    return orders;
  }

  @Override
  public List<Order> getAll() throws ServiceException {
    OrderDao orderDao = OrderDaoImpl.getInstance();
    List<Order> orders;
    try {
      orders = orderDao.findAll();
      if (orders == null) {
        LOGGER.warn("DAO returned null for getAll(), converting to empty list");
        return Collections.emptyList();
      }
    } catch (DaoException e) {
      LOGGER.error("Error fetching all orders", e);
      throw new ServiceException(e);
    }
    return orders;
  }

  @Override
  public Order getById(int id) throws ServiceException {
    if (id <= 0) {
      LOGGER.error("Invalid order id: " + id);
      throw new ServiceException("Invalid order id: " + id);
    }
    OrderDao orderDao = OrderDaoImpl.getInstance();
    Order order;
    try {
      Optional<Order> optionalOrder = orderDao.findById(id);
      order = optionalOrder.orElseThrow(() -> new ServiceException("Order not found"));
    } catch (DaoException e) {
      LOGGER.error("Error fetching order with id={}", id, e);
      throw new ServiceException(e);
    }
    return order;
  }

  @Override
  public void approve(Order order) throws ServiceException {
    if (order.getStatus() != Status.MODERATION) {
      LOGGER.warn("Order id={} cannot be approved. Current status={}", order.getId(), order.getStatus());
      throw new ServiceException("Order cannot be approved in status " + order.getStatus());
    }
    order.setStatus(Status.APPROVED);
    BigDecimal bill = calculateBill(order);
    order.setBill(bill);
    OrderDao orderDao = OrderDaoImpl.getInstance();
    try {
      orderDao.update(order);
    } catch (DaoException e) {
      LOGGER.error("Error approving order id={}", order.getId(), e);
      throw new ServiceException(e);
    }
  }

  private BigDecimal calculateBill(Order order) {
    BigDecimal bill = BigDecimal.ZERO;
    List<Procedure> procedures = order.getProcedures();
    if (procedures == null || procedures.isEmpty()) {
      return bill;
    }
    for (Procedure procedure : procedures) {
      if (procedure != null && procedure.getPrice() != null) {
        bill = bill.add(procedure.getPrice());
      }
    }
    return bill;
  }
}