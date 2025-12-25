package by.rabenok.webinnowise.command.impl;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.controller.PagePath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutCommand implements Command {
  @Override
  public String execute(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (session != null) {
      session.invalidate();
    }
    return PagePath.INDEX_JSP;
  }
}