package by.rabenok.webinnowise.command;

import by.rabenok.webinnowise.exception.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;


public interface Command {
  Logger LOGGER = LogManager.getLogger();

  String execute(HttpServletRequest request) throws CommandException;
}
