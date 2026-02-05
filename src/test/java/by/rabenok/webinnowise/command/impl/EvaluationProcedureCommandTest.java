package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.controller.RequestParameterName;
import by.rabenok.webinnowise.exception.CommandException;
import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.service.impl.ProcedureServiceImpl;
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
class EvaluationProcedureCommandTest {
  private static MockedStatic<ProcedureServiceImpl> procedureServiceStatic;
  @Mock
  private HttpServletRequest request;
  @Mock
  private ProcedureServiceImpl procedureService;
  private EvaluationProcedureCommand command;

  @BeforeAll
  static void setUpAll() {
    procedureServiceStatic = Mockito.mockStatic(ProcedureServiceImpl.class);
  }

  @BeforeEach
  void setUp() {
    command = new EvaluationProcedureCommand();
    procedureServiceStatic.when(ProcedureServiceImpl::getInstance).thenReturn(procedureService);
  }

  @AfterAll
  static void tearDownAll() {
    procedureServiceStatic.close();
  }

  @Test
  void execute_shouldReturnRatingProcedurePage_whenSuccess() throws Exception {
    when(request.getParameter(RequestParameterName.PROCEDURE_ID)).thenReturn("10");
    when(request.getParameter(RequestParameterName.RATING)).thenReturn("5");

    String actual = command.execute(request);

    assertEquals(PagePath.RATING_PROCEDURE, actual);
    verify(procedureService).evaluate("10", "5");
  }

  @Test
  void execute_shouldThrowCommandException_whenServiceFails() throws Exception {
    when(request.getParameter(RequestParameterName.PROCEDURE_ID)).thenReturn("10");
    when(request.getParameter(RequestParameterName.RATING)).thenReturn("5");
    doThrow(new ServiceException("DB error")).when(procedureService).evaluate("10", "5");

    assertThrows(CommandException.class, () -> command.execute(request));
  }

}