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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveOrderCommandTest {
  private static MockedStatic<OrderServiceImpl> orderServiceStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private OrderServiceImpl orderService;
  private RemoveOrderCommand command;

  @BeforeAll
  static void setUpAll() {
    orderServiceStatic = Mockito.mockStatic(OrderServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new RemoveOrderCommand();
    orderServiceStatic.when(OrderServiceImpl::getInstance).thenReturn(orderService);
  }

  @AfterAll
  static void tearDownAll() {
    orderServiceStatic.close();
  }

  @Test
  void execute_shouldSetSuccessMessage_whenOrderRemoved() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("1");
    when(orderService.remove(1)).thenReturn(true);

    String actual = command.execute(request);

    assertEquals(PagePath.ORDER_REMOVE_RESULT, actual);
    verify(request).setAttribute(RequestAttributeName.MSG, "Вы успешно отменили заявку.");
  }

  @Test
  void execute_shouldSetFailureMessage_whenOrderDoesNotExist() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("1");
    when(orderService.remove(1)).thenReturn(false);

    String actual = command.execute(request);

    assertEquals(PagePath.ORDER_REMOVE_RESULT, actual);
    verify(request).setAttribute(RequestAttributeName.MSG, "Такой заявки не существует. Удаление невозможно.");
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(request.getParameter(RequestParameterName.ORDER_ID)).thenReturn("1");
    doThrow(new ServiceException("DB error")).when(orderService).remove(1);

    assertThrows(CommandException.class, () -> command.execute(request));
  }
}