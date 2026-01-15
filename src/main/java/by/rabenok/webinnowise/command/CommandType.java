package by.rabenok.webinnowise.command;

import by.rabenok.webinnowise.command.impl.AdminOrdersCommand;
import by.rabenok.webinnowise.command.impl.ApproveOrderCommand;
import by.rabenok.webinnowise.command.impl.ClientOrdersCommand;
import by.rabenok.webinnowise.command.impl.DefaultCommand;
import by.rabenok.webinnowise.command.impl.EvaluationProcedureCommand;
import by.rabenok.webinnowise.command.impl.LoginCommand;
import by.rabenok.webinnowise.command.impl.LogoutCommand;
import by.rabenok.webinnowise.command.impl.NewOrderCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum CommandType {
  LOGIN(new LoginCommand()),
  LOGOUT(new LogoutCommand()),
  CLIENT_ORDERS(new ClientOrdersCommand()),
  NEW_ORDER(new NewOrderCommand()),
  EVALUATION(new EvaluationProcedureCommand()),
  APPROVE_ORDER(new ApproveOrderCommand()),
  ADMIN_ORDERS(new AdminOrdersCommand()),
  DEFAULT(new DefaultCommand());

  public static final Logger LOGGER = LogManager.getLogger();
  private final Command command;

  CommandType(Command command) {
    this.command = command;
  }

  public static Command define(String commandStr) {
    CommandType current;
    try {
      current = CommandType.valueOf(commandStr.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      LOGGER.info("The command {} does not exist", commandStr);
      current = CommandType.DEFAULT;
    }
    return current.command;
  }
}