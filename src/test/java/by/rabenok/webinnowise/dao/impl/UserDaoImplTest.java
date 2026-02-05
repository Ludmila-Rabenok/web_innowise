package by.rabenok.webinnowise.dao.impl;

import by.rabenok.webinnowise.model.Role;
import by.rabenok.webinnowise.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDaoImplTest {

  @Test
  void authenticate_shouldSelectPassword_whenUserExist() throws Exception {
    String expected = "Password_Luda_Test";
    String actual = UserDaoImpl.getInstance().authenticate("LudaTest");

    assertEquals(expected, actual);
  }

  @Test
  void authenticate_shouldReturnNullForUnknownUser() throws Exception {
    String actual = UserDaoImpl.getInstance().authenticate("UnknownUser");

    assertNull(actual);
  }

  @Test
  void authorize_shouldReturnRoleForExistingUser() throws Exception {
    Optional<Role> role = UserDaoImpl.getInstance().authorize("LudaTest");

    assertTrue(role.isPresent());
    assertEquals(Role.ADMIN, role.get());
  }

  @Test
  void authorize_shouldReturnEmptyForUnknownUser() throws Exception {
    Optional<Role> role = UserDaoImpl.getInstance().authorize("UnknownUser");

    assertFalse(role.isPresent());
  }

  @Test
  void findUserByName_shouldReturnUser() throws Exception {
    Optional<User> user = UserDaoImpl.getInstance().findUserByName("LudaTest");

    assertTrue(user.isPresent());
    assertEquals("LudaTest", user.get().getName());
    assertEquals(Role.ADMIN, user.get().getRole());
  }

  @Test
  void findUserByName_shouldReturnEmptyForUnknownUser() throws Exception {
    Optional<User> user = UserDaoImpl.getInstance().findUserByName("UnknownUser");

    assertFalse(user.isPresent());
  }

  @Test
  void findUserById_shouldReturnUser() throws Exception {
    Optional<User> user = UserDaoImpl.getInstance().findUserById(1);

    assertTrue(user.isPresent());
    assertEquals(1, user.get().getId());
    assertEquals("LudaTest", user.get().getName());
  }

  @Test
  void findUserById_shouldReturnEmptyForUnknownId() throws Exception {
    Optional<User> user = UserDaoImpl.getInstance().findUserById(9999);

    assertFalse(user.isPresent());
  }
}