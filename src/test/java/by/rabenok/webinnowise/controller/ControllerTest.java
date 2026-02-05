package by.rabenok.webinnowise.controller;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.command.CommandType;
import by.rabenok.webinnowise.exception.CommandException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ControllerTest {
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private RequestDispatcher dispatcher;
  @Mock
  private Command command;
  static MockedStatic<CommandType> commandTypeMock;
  private Controller controller;

  @BeforeAll
  static void setUpAll() {
    commandTypeMock = Mockito.mockStatic(CommandType.class);
  }

  @BeforeEach
  void setUp() {
    controller = new Controller();
  }

  @AfterAll
  static void tearDownAll() {
    commandTypeMock.close();
  }

  @Test
  void processRequest_shouldForward_whenCommandReturnPage() throws Exception {
    when(request.getParameter(RequestParameterName.COMMAND)).thenReturn("login");
    commandTypeMock.when(() -> CommandType.define("login")).thenReturn(command);
    when(command.execute(request)).thenReturn("/page.jsp");
    when(request.getRequestDispatcher("/page.jsp")).thenReturn(dispatcher);

    controller.doGet(request, response);

    verify(dispatcher).forward(request, response);
    verify(response, never()).sendRedirect(any());
  }

  @Test
  void processRequest_shouldRedirect_whenCommandReturnRedirect() throws Exception {
    when(request.getParameter(RequestParameterName.COMMAND)).thenReturn("logout");
    commandTypeMock.when(() -> CommandType.define("logout")).thenReturn(command);
    when(command.execute(request)).thenReturn("redirect:/home");

    controller.doPost(request, response);

    verify(response).sendRedirect("/home");
    verify(request, never()).getRequestDispatcher(any());
  }

  @Test
  void processRequest_shouldForwardToError500_whenCommandThrowsException() throws Exception {
    when(request.getParameter(RequestParameterName.COMMAND)).thenReturn("bad");
    commandTypeMock.when(() -> CommandType.define("bad")).thenReturn(command);
    when(command.execute(request)).thenThrow(new CommandException("fail"));
    when(request.getRequestDispatcher(PagePath.ERROR_500)).thenReturn(dispatcher);

    controller.doGet(request, response);

    verify(request).setAttribute(eq(RequestAttributeName.ERROR_MSG), any());
    verify(dispatcher).forward(request, response);
  }
}