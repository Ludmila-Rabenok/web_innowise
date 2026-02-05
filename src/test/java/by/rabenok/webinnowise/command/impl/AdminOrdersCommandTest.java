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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOrdersCommandTest {
  private static MockedStatic<OrderServiceImpl> orderServiceStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpSession session;
  @Mock
  private OrderServiceImpl orderService;
  private AdminOrdersCommand command;

  @BeforeAll
  static void setUpAll() {
    orderServiceStatic = Mockito.mockStatic(OrderServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new AdminOrdersCommand();
    orderServiceStatic.when(OrderServiceImpl::getInstance).thenReturn(orderService);
    when(request.getSession()).thenReturn(session);
  }

  @AfterAll
  static void tearDownAll() {
    orderServiceStatic.close();
  }

  @Test
  void execute_shouldReturnAdminOrdersPage_whenRoleIsAdmin() throws Exception {
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("ADMIN");
    List<Order> orders = new ArrayList<>();
    orders.add(new Order());
    when(orderService.findAll()).thenReturn(orders);

    String actual = command.execute(request);

    assertEquals(PagePath.ADMIN_ORDERS, actual);
    verify(orderService).findAll();
    verify(request).setAttribute(RequestAttributeName.ORDERS, orders);
  }

  @Test
  void execute_shouldReturnIndexPage_whenRoleIsNotAdmin() throws Exception {
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("CLIENT");

    String actual = command.execute(request);

    assertEquals(PagePath.INDEX_JSP, actual);
    verify(orderService, never()).findAll();
    verify(request, never()).setAttribute(eq(RequestAttributeName.ORDERS), any());
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("ADMIN");
    when(orderService.findAll()).thenThrow(new ServiceException("DB error"));

    assertThrows(CommandException.class, () -> command.execute(request));
  }
}