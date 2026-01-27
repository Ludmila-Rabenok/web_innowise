package by;

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
  public boolean remove(int id) throws ServiceException {
    boolean removed;
    try {
      removed = OrderDaoImpl.getInstance().remove(id);
      if (removed) {
        LOGGER.info("Order with id={} successfully removed", id);
      } else {
        LOGGER.warn("Order with id={} not found, nothing removed", id);
      }
    } catch (DaoException e) {
      LOGGER.error("Error removed order with id={}", id, e);
      throw new ServiceException(e);
    }
    return removed;
  }

  @Override
  public void approve(int id) throws ServiceException {
    Order order = findById(id);
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
      LOGGER.error("Error approving order id={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void reject(int id) throws ServiceException {
    Order order = findById(id);
    if (order.getStatus() != Status.MODERATION) {
      LOGGER.warn("Order id={} cannot be rejected. Current status={}", id, order.getStatus());
      throw new ServiceException("Order cannot be rejected in status " + order.getStatus());
    }
    order.setStatus(Status.REJECTED);
    BigDecimal bill = calculateBill(order);
    order.setBill(bill);
    OrderDao orderDao = OrderDaoImpl.getInstance();
    try {
      orderDao.updateStatusAndBill(order);
    } catch (DaoException e) {
      LOGGER.error("Error rejected order id={}", id, e);
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