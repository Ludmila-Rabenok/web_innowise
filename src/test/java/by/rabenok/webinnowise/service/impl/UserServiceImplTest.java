package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.impl.UserDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.util.PasswordBCrypt;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
  private static MockedStatic<UserDaoImpl> userDaoMockedStatic;
  private final UserServiceImpl userService = UserServiceImpl.getInstance();
  @Mock
  private UserDaoImpl userDao;

  @BeforeAll
  static void setUpAll() {
    userDaoMockedStatic = Mockito.mockStatic(UserDaoImpl.class);
  }

  @BeforeEach
  void setUp() {
    userDaoMockedStatic.when(UserDaoImpl::getInstance).thenReturn(userDao);
  }

  @AfterAll
  static void tearDownAll() {
    userDaoMockedStatic.close();
  }

  @Test
  void authenticate_shouldReturnTrue_whenUserExists() throws Exception {
    when(userDao.authenticate("Login")).thenReturn("Hash");
    try (MockedStatic<PasswordBCrypt> passwordBCryptMockedStatic = Mockito.mockStatic(PasswordBCrypt.class)) {
      passwordBCryptMockedStatic
              .when(() -> PasswordBCrypt.verifyPassword("Pass", "Hash"))
              .thenReturn(true);

      boolean actual = userService.authenticate("Login", "Pass");

      assertTrue(actual);
      verify(userDao).authenticate("Login");
    }
  }

  @Test
  void authenticate_shouldReturnFalse_whenUserNotFound() throws Exception {
    when(userDao.authenticate("Login")).thenReturn(null);
    try (MockedStatic<PasswordBCrypt> passwordBCryptMockedStatic = Mockito.mockStatic(PasswordBCrypt.class)) {

      boolean actual = userService.authenticate("Login", "Pass");

      assertFalse(actual);
      verify(userDao).authenticate("Login");
      passwordBCryptMockedStatic.verifyNoInteractions();
    }
  }

  @Test
  void authenticate_shouldReturnFalse_whenPasswordIncorrect() throws Exception {
    when(userDao.authenticate("Login")).thenReturn("Hash");
    try (MockedStatic<PasswordBCrypt> passwordBCryptMockedStatic = Mockito.mockStatic(PasswordBCrypt.class)) {
      passwordBCryptMockedStatic
              .when(() -> PasswordBCrypt.verifyPassword("WrongPass", "Hash"))
              .thenReturn(false);

      boolean actual = userService.authenticate("Login", "WrongPass");

      assertFalse(actual);
      verify(userDao).authenticate("Login");
      passwordBCryptMockedStatic.verify(() -> PasswordBCrypt.verifyPassword("WrongPass", "Hash"));
    }
  }

  @Test
  void authenticate_shouldThrowServiceException_whenUserDaoFails() throws Exception {
    when(userDao.authenticate("Login")).thenThrow(new DaoException("DB error"));
    try (MockedStatic<PasswordBCrypt> passwordBCryptMockedStatic = Mockito.mockStatic(PasswordBCrypt.class)) {

      ServiceException exception = assertThrows(ServiceException.class,
              () -> userService.authenticate("Login", "Pass"));

      assertEquals("DB error", exception.getCause().getMessage());
      passwordBCryptMockedStatic.verifyNoInteractions();
      verify(userDao).authenticate("Login");
    }
  }

  @Test
  void authorize_shouldReturnRole_whenUserExists() throws Exception {
    Optional<Role> expected = Optional.of(Role.ADMIN);
    when(userDao.authorize("Login")).thenReturn(expected);

    Optional<Role> actual = userService.authorize("Login");

    assertEquals(expected, actual);
    verify(userDao).authorize("Login");
  }

  @Test
  void authorize_shouldReturnEmpty_whenUserNotFound() throws Exception {
    when(userDao.authorize("Login")).thenReturn(Optional.empty());

    Optional<Role> actual = userService.authorize("Login");

    assertEquals(Optional.empty(), actual);
    verify(userDao).authorize("Login");
  }

  @Test
  void authorize_shouldThrowServiceException_whenDaoFails() throws Exception {
    when(userDao.authorize("Login")).thenThrow(new DaoException("DB error"));

    ServiceException exception = assertThrows(ServiceException.class, () -> userService.authorize("Login"));

    assertEquals("DB error", exception.getCause().getMessage());
    verify(userDao).authorize("Login");
  }
}