package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;

import javax.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {

  @Override
  public String execute(HttpServletRequest request) {
    return PagePath.INDEX_JSP;
  }
}