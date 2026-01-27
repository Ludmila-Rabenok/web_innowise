package by.rabenok.webinnowise.command;

import by.rabenok.webinnowise.command.impl.AdminOrdersCommand;
import by.rabenok.webinnowise.command.impl.ApproveOrderCommand;
import by.rabenok.webinnowise.command.impl.ClientOrdersCommand;
import by.rabenok.webinnowise.command.impl.DefaultCommand;
import by.rabenok.webinnowise.command.impl.EvaluationProcedureCommand;
import by.rabenok.webinnowise.command.impl.LoginCommand;
import by.rabenok.webinnowise.command.impl.LogoutCommand;
import by.rabenok.webinnowise.command.impl.NewOrderCommand;
import by.rabenok.webinnowise.command.impl.RejectOrderCommand;
import by.rabenok.webinnowise.command.impl.RemoveOrderCommand;
import by.rabenok.webinnowise.command.impl.SuccessOrderCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum CommandType {
  LOGIN(new LoginCommand()),
  LOGOUT(new LogoutCommand()),
  CLIENT_ORDERS(new ClientOrdersCommand()),
  NEW_ORDER(new NewOrderCommand()),
  SUCCESS_ORDER(new SuccessOrderCommand()),
  REMOVE_ORDER(new RemoveOrderCommand()),
  EVALUATION(new EvaluationProcedureCommand()),
  ADMIN_ORDERS(new AdminOrdersCommand()),
  APPROVE_ORDER(new ApproveOrderCommand()),
  REJECT_ORDER(new RejectOrderCommand()),
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