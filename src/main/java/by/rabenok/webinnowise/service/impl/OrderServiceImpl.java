package by.rabenok.webinnowise.service.impl;

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
    User user;
    try {
      user = UserDaoImpl.getInstance().findUserByName(userName)
              .orElseThrow(() -> new ServiceException("User not found"));
      order.setUser(user);
      LocalDate localDate = LocalDate.parse(date);
      LocalTime localTime = LocalTime.parse(time);
      LocalDateTime leadTime = LocalDateTime.of(localDate, localTime);
      order.setLeadTime(leadTime);
      OrderDaoImpl.getInstance().save(order, procedures);
    } catch (DaoException e) {
      LOGGER.error("Error creating order", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Order> findOrdersByUserName(String userName) throws ServiceException {
    try {
      return OrderDaoImpl.getInstance().findOrdersByUserName(userName);
    } catch (DaoException e) {
      LOGGER.error("Error fetching orders for user {}", userName, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Order> findAll() throws ServiceException {
    try {
      return OrderDaoImpl.getInstance().findAll();
    } catch (DaoException e) {
      LOGGER.error("Error fetching all orders", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Order findById(int id) throws ServiceException {
    try {
      Optional<Order> optionalOrder = OrderDaoImpl.getInstance().findById(id);
      return optionalOrder.orElseThrow(() -> new ServiceException("Order not found"));
    } catch (DaoException e) {
      LOGGER.error("Error fetching order with id={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean remove(int id) throws ServiceException {
    boolean isRemoved;
    try {
      isRemoved = OrderDaoImpl.getInstance().remove(id);
      if (isRemoved) {
        LOGGER.info("Order with id={} successfully removed", id);
      } else {
        LOGGER.warn("Order with id={} not found, nothing removed", id);
      }
    } catch (DaoException e) {
      LOGGER.error("Error removed order with id={}", id, e);
      throw new ServiceException(e);
    }
    return isRemoved;
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
    try {
      boolean isUpdated = OrderDaoImpl.getInstance().updateStatusAndBill(order);
      if (!isUpdated) {
        LOGGER.warn("Order id={} was not updated in DB", id);
        throw new ServiceException("Order was not updated");
      }
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
    try {
      boolean isUpdated = OrderDaoImpl.getInstance().updateStatusAndBill(order);
      if (!isUpdated) {
        LOGGER.warn("Order id={} was not updated in DB", id);
        throw new ServiceException("Order was not updated");
      }
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