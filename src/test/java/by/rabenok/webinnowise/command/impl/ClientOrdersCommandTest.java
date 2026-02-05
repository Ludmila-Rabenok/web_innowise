package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientOrdersCommandTest {
  private static MockedStatic<OrderServiceImpl> orderServiceStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpSession session;
  @Mock
  private OrderServiceImpl orderService;
  private ClientOrdersCommand command;

  @BeforeAll
  static void setUpAll() {
    orderServiceStatic = Mockito.mockStatic(OrderServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new ClientOrdersCommand();
    orderServiceStatic.when(OrderServiceImpl::getInstance).thenReturn(orderService);
    when(request.getSession()).thenReturn(session);
  }

  @AfterAll
  static void tearDownAll() {
    orderServiceStatic.close();
  }

  @Test
  void execute_shouldReturnClientOrdersPage_whenSuccess() throws Exception {
    when(session.getAttribute(RequestAttributeName.USER)).thenReturn("anna");
    List<Order> orders = new ArrayList<>();
    orders.add(new Order());
    when(orderService.findOrdersByUserName("anna")).thenReturn(orders);

    String actual = command.execute(request);

    assertEquals(PagePath.CLIENT_ORDERS, actual);
    verify(orderService).findOrdersByUserName("anna");
    verify(request).setAttribute(RequestAttributeName.ORDERS, orders);
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(session.getAttribute(RequestAttributeName.USER)).thenReturn("anna");
    when(orderService.findOrdersByUserName("anna")).thenThrow(new ServiceException("DB error"));

    assertThrows(CommandException.class, () -> command.execute(request));
  }
}