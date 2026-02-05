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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApproveOrderCommandTest {
  private static MockedStatic<OrderServiceImpl> orderServiceMockedStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpSession session;
  @Mock
  private OrderServiceImpl orderService;

  private ApproveOrderCommand command;

  @BeforeAll
  static void setUpAll() {
    orderServiceMockedStatic = Mockito.mockStatic(OrderServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new ApproveOrderCommand();
    orderServiceMockedStatic.when(OrderServiceImpl::getInstance).thenReturn(orderService);
    when(request.getSession()).thenReturn(session);
  }

  @AfterAll
  static void tearDownAll() {
    orderServiceMockedStatic.close();
  }

  @Test
  void execute_shouldReturnMainAdminPage_whenRoleIsAdmin() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("1");
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("ADMIN");

    String actual = command.execute(request);

    assertEquals(PagePath.MAIN_ADMIN, actual);
    verify(orderService).approve(1);
  }

  @Test
  void execute_shouldReturnIndexPage_whenRoleIsNotAdmin() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("1");
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("CLIENT");

    String actual = command.execute(request);

    assertEquals(PagePath.INDEX_JSP, actual);
    verify(orderService, never()).approve(1);
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("1");
    when(session.getAttribute(RequestAttributeName.ROLE)).thenReturn("ADMIN");
    doThrow(new ServiceException("DB error")).when(orderService).approve(1);

    assertThrows(CommandException.class, () -> command.execute(request));
  }
}