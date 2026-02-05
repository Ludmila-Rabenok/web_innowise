package by.rabenok.webinnowise.service.impl;

import by.rabenok.webinnowise.dao.impl.ProcedureDaoImpl;
import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.exception.ServiceException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcedureServiceImplTest {
  @Mock
  private ProcedureDaoImpl procedureDao;

  private static MockedStatic<ProcedureDaoImpl> procedureDaoMockedStatic;

  private final ProcedureServiceImpl service = ProcedureServiceImpl.getInstance();

  @BeforeAll
  static void setUpAll() {
    procedureDaoMockedStatic = Mockito.mockStatic(ProcedureDaoImpl.class);
  }

  @BeforeEach
  void setUp() {
    procedureDaoMockedStatic.when(ProcedureDaoImpl::getInstance).thenReturn(procedureDao);
  }

  @AfterAll
  static void tearDownAll() {
    procedureDaoMockedStatic.close();
  }

  @Test
  void evaluate_shouldAddRatingToProcedure() throws Exception {
    when(procedureDao.addRating(1, 5)).thenReturn(true);

    assertDoesNotThrow(() -> service.evaluate("1", "5"));

    verify(procedureDao).addRating(1, 5);
  }

  @Test
  void evaluate_shouldThrowServiceException_whenNotParseProcedureIdStringToInt() {
    assertThrows(ServiceException.class, () -> {
      service.evaluate("Wrong", "5");
    });

    verifyNoInteractions(procedureDao);
  }

  @Test
  void evaluate_shouldThrowServiceException_whenNotParseRatingStringToInt() {
    assertThrows(ServiceException.class, () -> {
      service.evaluate("1", "Wrong");
    });

    verifyNoInteractions(procedureDao);
  }

  @Test
  void evaluate_shouldThrowServiceException_whenProcedureDaoReturnFalse() throws Exception {
    when(procedureDao.addRating(1, 5)).thenReturn(false);

    assertThrows(ServiceException.class, () -> service.evaluate("1", "5"));
    verify(procedureDao).addRating(1, 5);
  }

  @Test
  void evaluate_shouldThrowServiceException_whenProcedureDaoFails() throws Exception {
    when(procedureDao.addRating(1, 5)).thenThrow(new DaoException("DB error"));

    assertThrows(ServiceException.class, () -> service.evaluate("1", "5"));
    verify(procedureDao).addRating(1, 5);
  }
}