package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RejectOrderCommandTest {
  private static MockedStatic<OrderServiceImpl> orderServiceStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpSession session;
  @Mock
  private OrderServiceImpl orderService;
  private RejectOrderCommand command;

  @BeforeAll
  static void setUpAll() {
    orderServiceStatic = Mockito.mockStatic(OrderServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new RejectOrderCommand();
    orderServiceStatic.when(OrderServiceImpl::getInstance).thenReturn(orderService);
    when(request.getSession()).thenReturn(session);
  }

  @AfterAll
  static void tearDownAll() {
    orderServiceStatic.close();
  }

  @Test
  void execute_shouldReturnMainAdminPage_whenRoleIsAdmin() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("5");
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("ADMIN");

    String actual = command.execute(request);

    assertEquals(PagePath.MAIN_ADMIN, actual);
    verify(orderService).reject(5);
  }

  @Test
  void execute_shouldReturnIndexPage_whenRoleIsNotAdmin() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("5");
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("CLIENT");

    String actual = command.execute(request);

    assertEquals(PagePath.INDEX_JSP, actual);
    verify(orderService, never()).reject(anyInt());
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("5");
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("ADMIN");
    doThrow(new ServiceException("DB error")).when(orderService).reject(5);

    assertThrows(CommandException.class, () -> command.execute(request));
  }
}