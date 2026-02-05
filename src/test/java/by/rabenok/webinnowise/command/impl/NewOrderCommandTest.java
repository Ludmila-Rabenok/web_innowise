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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewOrderCommandTest {
  private static MockedStatic<OrderServiceImpl> orderServiceStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpSession session;
  @Mock
  private OrderServiceImpl orderService;
  private NewOrderCommand command;

  @BeforeAll
  static void setUpAll() {
    orderServiceStatic = Mockito.mockStatic(OrderServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new NewOrderCommand();
    orderServiceStatic.when(OrderServiceImpl::getInstance).thenReturn(orderService);
  }

  @AfterAll
  static void tearDownAll() {
    orderServiceStatic.close();
  }

  @Test
  void execute_shouldReturnIndexPage_whenUserNotLoggedIn() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(RequestAttributeName.USER)).thenReturn(null);

    String actual = command.execute(request);

    assertEquals(PagePath.INDEX_JSP, actual);
    verify(request).setAttribute(RequestAttributeName.LOGIN_MSG, "Authentication error");
    verify(orderService, never()).createOrder(any(), any(), any(), any());
  }

  @Test
  void execute_shouldReturnMainClientPage_whenNoProcedures() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(RequestAttributeName.USER)).thenReturn("anna");
    when(request.getParameterValues(RequestParameterName.PROCEDURES)).thenReturn(null);

    String actual = command.execute(request);

    assertEquals(PagePath.MAIN_CLIENT, actual);
    verify(request).setAttribute(RequestAttributeName.ORDER_ERROR, "At least one procedure required");
    verify(orderService, never()).createOrder(any(), any(), any(), any());
  }

  @Test
  void execute_shouldReturnMainClientPage_whenDateOrTimeMissing() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(RequestAttributeName.USER)).thenReturn("anna");
    when(request.getParameterValues(RequestParameterName.PROCEDURES)).thenReturn(new String[]{"1"});
    when(request.getParameter(RequestParameterName.DATE)).thenReturn("");
    when(request.getParameter(RequestParameterName.TIME)).thenReturn("10:00");

    String actual = command.execute(request);

    assertEquals(PagePath.MAIN_CLIENT, actual);
    verify(request).setAttribute(RequestAttributeName.ORDER_ERROR, "Enter date and time");
    verify(orderService, never()).createOrder(any(), any(), any(), any());
  }

  @Test
  void execute_shouldRedirect_whenOrderCreatedSuccessfully() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(RequestAttributeName.USER)).thenReturn("anna");
    when(request.getParameterValues(RequestParameterName.PROCEDURES)).thenReturn(new String[]{"1", "2"});
    when(request.getParameter(RequestParameterName.DATE)).thenReturn("2025-01-01");
    when(request.getParameter(RequestParameterName.TIME)).thenReturn("12:00");

    String actual = command.execute(request);

    assertEquals("redirect:/controller?command=success_order", actual);
    verify(orderService).createOrder("anna", new String[]{"1", "2"}, "2025-01-01", "12:00");
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(RequestAttributeName.USER)).thenReturn("anna");
    when(request.getParameterValues(RequestParameterName.PROCEDURES)).thenReturn(new String[]{"1"});
    when(request.getParameter(RequestParameterName.DATE)).thenReturn("2025-01-01");
    when(request.getParameter(RequestParameterName.TIME)).thenReturn("12:00");
    doThrow(new ServiceException("DB error")).when(orderService).createOrder("anna", new String[]{"1"}, "2025-01-01", "12:00");

    assertThrows(CommandException.class, () -> command.execute(request));
  }
}