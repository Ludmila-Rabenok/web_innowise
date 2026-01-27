package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;
import by.rabenok.webinnowise.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

public class SuccessOrderCommand implements Command {
  @Override
  public String execute(HttpServletRequest request) throws CommandException {
    return PagePath.SUCCESS_ORDER;
  }
}
