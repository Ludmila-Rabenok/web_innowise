package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.impl.OrderDaoImpl;
import by.rabenok.webinnowise.dao.impl.UserDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.Procedure;
import by.rabenok.webinnowise.model.Status;
import by.rabenok.webinnowise.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
  static MockedStatic<UserDaoImpl> userDaoMockedStatic;
  static MockedStatic<OrderDaoImpl> orderDaoMockedStatic;
  @Mock
  private UserDaoImpl userDao;
  @Mock
  private OrderDaoImpl orderDao;
  @Mock
  private User user;
  private final OrderServiceImpl service = OrderServiceImpl.getInstance();
  private Order order;

  @BeforeAll
  static void setUpAll() {
    userDaoMockedStatic = Mockito.mockStatic(UserDaoImpl.class);
    orderDaoMockedStatic = Mockito.mockStatic(OrderDaoImpl.class);
  }

  @BeforeEach
  void setup() {
    userDaoMockedStatic.when(UserDaoImpl::getInstance).thenReturn(userDao);
    orderDaoMockedStatic.when(OrderDaoImpl::getInstance).thenReturn(orderDao);

    order = new Order();
    order.setId(1);
    order.setStatus(Status.MODERATION);
    Procedure procedure1 = new Procedure();
    Procedure procedure2 = new Procedure();
    procedure1.setPrice(new BigDecimal("1"));
    procedure2.setPrice(new BigDecimal("2"));
    List<Procedure> procedures = new ArrayList<>();
    procedures.add(procedure1);
    procedures.add(procedure2);
    order.setProcedures(procedures);
  }

  @AfterAll
  static void tearDownAll() {
    userDaoMockedStatic.close();
    orderDaoMockedStatic.close();
  }

  @Test
  void createOrder_shouldCreateOrder() throws Exception {
    when(userDao.findUserByName("anna")).thenReturn(Optional.of(user));
    doNothing().when(orderDao).save(any(Order.class), any(String[].class));

    service.createOrder("anna", new String[]{"procedure"}, "2025-01-01", "10:00");

    verify(userDao).findUserByName("anna");
    verify(orderDao).save(any(Order.class), any(String[].class));
  }

  @Test
  void createOrder_shouldThrowServiceException_whenUserNotFound() throws Exception {
    when(userDao.findUserByName("anna")).thenReturn(Optional.empty());

    assertThrows(ServiceException.class,
            () -> service.createOrder("anna", new String[]{"procedure"}, "2025-01-01", "10:00"));
  }

  @Test
  void createOrder_shouldThrowServiceException_whenUserDaoFails() throws Exception {
    when(userDao.findUserByName("anna")).thenThrow(new DaoException("DB error"));

    assertThrows(ServiceException.class,
            () -> service.createOrder("anna", new String[]{"procedure"}, "2025-01-01", "10:00"));
  }

  @Test
  void createOrder_shouldThrowServiceException_whenOrderDaoFails() throws Exception {
    when(userDao.findUserByName("anna")).thenReturn(Optional.of(user));
    doThrow(new DaoException("DB error"))
            .when(orderDao)
            .save(any(Order.class), any(String[].class));

    assertThrows(ServiceException.class,
            () -> service.createOrder("anna", new String[]{"procedure"}, "2025-01-01", "10:00"));
  }

  @Test
  void findOrdersByUserName_shouldFindOrders() throws Exception {
    List<Order> expected = new ArrayList<>();
    expected.add(order);
    when(orderDao.findOrdersByUserName("anna")).thenReturn(expected);

    List<Order> actual = service.findOrdersByUserName("anna");

    assertEquals(expected, actual);
    verify(orderDao).findOrdersByUserName("anna");
  }

  @Test
  void findOrdersByUserName_shouldThrowServiceException_whenOrderDaoFails() throws Exception {
    when(orderDao.findOrdersByUserName("anna")).thenThrow(new DaoException("DB error"));

    assertThrows(ServiceException.class, () -> service.findOrdersByUserName("anna"));
  }

  @Test
  void findById_shouldFindOrderById() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.of(order));

    Order actual = service.findById(1);

    assertEquals(order, actual);
  }

  @Test
  void findById_shouldThrowServiceException_whenOrderNotFound() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.empty());

    assertThrows(ServiceException.class, () -> service.findById(1));
  }

  @Test
  void findById_shouldThrowServiceException_whenOrderDaoFails() throws Exception {
    when(orderDao.findById(1)).thenThrow(new DaoException("DB error"));

    assertThrows(ServiceException.class, () -> service.findById(1));
  }

  @Test
  void remove_shouldRemoveOrder() throws Exception {
    when(orderDao.remove(1)).thenReturn(true);

    boolean actual = service.remove(1);

    assertTrue(actual);
  }

  @Test
  void remove_shouldReturnFalse_whenOrderNotFound() throws Exception {
    when(orderDao.remove(1)).thenReturn(false);

    boolean actual = service.remove(1);

    assertFalse(actual);
  }

  @Test
  void remove_shouldThrowServiceException_whenOrderDaoFails() throws Exception {
    when(orderDao.remove(1)).thenThrow(new DaoException("DB error"));

    assertThrows(ServiceException.class, () -> service.remove(1));
  }

  @Test
  void approve_shouldUpdateStatusAndBill_whenSuccess() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.of(order));
    when(orderDao.updateStatusAndBill(any())).thenReturn(true);

    service.approve(1);

    assertAll(() -> assertEquals(Status.APPROVED, order.getStatus()),
            () -> assertEquals(new BigDecimal("3"), order.getBill()));
    verify(orderDao).updateStatusAndBill(order);
  }

  @ParameterizedTest
  @EnumSource(value = Status.class, names = {"APPROVED", "REJECTED"})
  void approve_shouldThrowServiceException_whenStatusNotModeration(Status status) throws Exception {
    order.setStatus(status);
    when(orderDao.findById(1)).thenReturn(Optional.of(order));

    assertThrows(ServiceException.class, () -> service.approve(1));
    verify(orderDao, never()).updateStatusAndBill(any());
  }

  @Test
  void approve_shouldThrowServiceException_whenOrderDaoFails() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.of(order));
    when(orderDao.updateStatusAndBill(any())).thenThrow(new DaoException("DB error"));

    assertThrows(ServiceException.class, () -> service.approve(1));
  }

  @Test
  void approve_shouldThrowServiceException_whenOrderDaoUpdateReturnFalse() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.of(order));
    when(orderDao.updateStatusAndBill(any())).thenReturn(false);

    assertThrows(ServiceException.class, () -> service.approve(1));
  }

  @Test
  void reject_shouldUpdateStatusAndBill_whenSuccess() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.of(order));
    when(orderDao.updateStatusAndBill(any())).thenReturn(true);

    service.reject(1);

    assertAll(() -> assertEquals(Status.REJECTED, order.getStatus()),
            () -> assertEquals(new BigDecimal("3"), order.getBill()));
    verify(orderDao).updateStatusAndBill(order);
  }

  @ParameterizedTest
  @EnumSource(value = Status.class, names = {"APPROVED", "REJECTED"})
  void reject_shouldThrowServiceException_whenStatusNotModeration(Status status) throws Exception {
    order.setStatus(status);
    when(orderDao.findById(1)).thenReturn(Optional.of(order));

    assertThrows(ServiceException.class, () -> service.reject(1));
    verify(orderDao, never()).updateStatusAndBill(any());
  }

  @Test
  void reject_shouldThrowServiceException_whenOrderDaoFails() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.of(order));
    when(orderDao.updateStatusAndBill(any())).thenThrow(new DaoException("DB error"));

    assertThrows(ServiceException.class, () -> service.reject(1));
  }

  @Test
  void reject_shouldThrowServiceException_whenOrderDaoUpdateReturnFalse() throws Exception {
    when(orderDao.findById(1)).thenReturn(Optional.of(order));
    when(orderDao.updateStatusAndBill(any())).thenReturn(false);

    assertThrows(ServiceException.class, () -> service.reject(1));
  }

}