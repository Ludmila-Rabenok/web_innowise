package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.OrderDao;
import by.rabenok.webinnowise.dao.UserDao;
import by.rabenok.webinnowise.dao.impl.OrderDaoImpl;
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
  public void createOrder(String userName, String[] procedures, String date, String time) throws ServiceException {
    if (userName == null || userName.isEmpty()) {
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
      user = userDao.findUserByName(userName)
              .orElseThrow(() -> new ServiceException("User not found"));
      order.setUser(user);
      LocalDate localDate = LocalDate.parse(date);
      LocalTime localTime = LocalTime.parse(time);
      LocalDateTime leadTime = LocalDateTime.of(localDate, localTime);
      order.setLeadTime(leadTime);
      OrderDao orderDao = OrderDaoImpl.getInstance();
      orderDao.save(order, procedures);
    } catch (DaoException e) {
      LOGGER.error("Error creating order", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Order> findOrdersByUserName(String userName) throws ServiceException {
    OrderDao orderDao = OrderDaoImpl.getInstance();
    List<Order> orders;
    try {
      orders = orderDao.findOrdersByUserName(userName);
      if (orders == null) {
        orders = Collections.emptyList();
      }
    } catch (DaoException e) {
      LOGGER.error("Error fetching orders for user {}", userName, e);
      throw new ServiceException(e);
    }
    return orders;
  }

  @Override
  public List<Order> findAll() throws ServiceException {
    List<Order> orders;
    try {
      orders = OrderDaoImpl.getInstance().findAll();
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
  public Order findById(int id) throws ServiceException {
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
  public void approve(int orderId) throws ServiceException {
    Order order = findById(orderId);
    if (order.getStatus() != Status.MODERATION) {
      LOGGER.warn("Order id={} cannot be approved. Current status={}", order.getId(), order.getStatus());
      throw new ServiceException("Order cannot be approved in status " + order.getStatus());
    }
    order.setStatus(Status.APPROVED);
    BigDecimal bill = calculateBill(order);
    order.setBill(bill);
    OrderDao orderDao = OrderDaoImpl.getInstance();
    try {
      orderDao.updateStatusAndBill(order);
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