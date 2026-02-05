package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestAttributeName;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.service.impl.UserServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginCommandTest {
  private static MockedStatic<UserServiceImpl> userServiceStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpSession session;
  @Mock
  private UserServiceImpl userService;
  private LoginCommand command;

  @BeforeAll
  static void setUpAll() {
    userServiceStatic = Mockito.mockStatic(UserServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new LoginCommand();
    userServiceStatic.when(UserServiceImpl::getInstance).thenReturn(userService);
  }

  @AfterAll
  static void tearDownAll() {
    userServiceStatic.close();
  }

  @Test
  void execute_shouldReturnIndexPage_whenLoginOrPasswordEmpty() throws Exception {
    when(request.getParameter(RequestParameterName.LOGIN)).thenReturn("");
    when(request.getParameter(RequestParameterName.PASS)).thenReturn("123");

    String actual = command.execute(request);

    assertEquals(PagePath.INDEX_JSP, actual);
    verify(request).setAttribute(RequestAttributeName.LOGIN_MSG, "Enter login and password");
    verify(userService, never()).authenticate(any(), any());
  }

  @Test
  void execute_shouldReturnIndexPage_whenAuthenticationFails() throws Exception {
    when(request.getParameter(RequestParameterName.LOGIN)).thenReturn("anna");
    when(request.getParameter(RequestParameterName.PASS)).thenReturn("wrong");
    when(userService.authenticate("anna", "wrong")).thenReturn(false);

    String actual = command.execute(request);

    assertEquals(PagePath.INDEX_JSP, actual);
    verify(request).setAttribute(RequestAttributeName.LOGIN_MSG, "Authentication error");
    verify(userService).authenticate("anna", "wrong");
    verify(userService, never()).authorize(any());
  }

  @Test
  void execute_shouldReturnMainClientPage_whenClientLogIn() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(request.getParameter(RequestParameterName.LOGIN)).thenReturn("anna");
    when(request.getParameter(RequestParameterName.PASS)).thenReturn("123");
    when(userService.authenticate("anna", "123")).thenReturn(true);
    when(userService.authorize("anna")).thenReturn(Optional.of(Role.CLIENT));

    String actual = command.execute(request);

    assertEquals(PagePath.MAIN_CLIENT, actual);
    verify(session).setAttribute(RequestAttributeName.USER, "anna");
    verify(session).setAttribute(RequestAttributeName.ROLE, "CLIENT");
  }

  @Test
  void execute_shouldReturnMainAdminPage_whenAdminLogIn() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(request.getParameter(RequestParameterName.LOGIN)).thenReturn("admin");
    when(request.getParameter(RequestParameterName.PASS)).thenReturn("123");
    when(userService.authenticate("admin", "123")).thenReturn(true);
    when(userService.authorize("admin")).thenReturn(Optional.of(Role.ADMIN));

    String actual = command.execute(request);

    assertEquals(PagePath.MAIN_ADMIN, actual);
    verify(session).setAttribute(RequestAttributeName.USER, "admin");
    verify(session).setAttribute(RequestAttributeName.ROLE, "ADMIN");
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(request.getParameter(RequestParameterName.LOGIN)).thenReturn("anna");
    when(request.getParameter(RequestParameterName.PASS)).thenReturn("123");
    when(userService.authenticate("anna", "123")).thenThrow(new ServiceException("DB error"));

    assertThrows(CommandException.class, () -> command.execute(request));
  }
}