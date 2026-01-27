package by.rabenok.webinnowise.controller;

import by.rabenok.webinnowise.command.Command;
import by.rabenok.webinnowise.command.CommandType;
import by.rabenok.webinnowise.exception.CommandException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/html");
    String commandStr = request.getParameter(RequestParameterName.COMMAND);
    Command command = CommandType.define(commandStr);
    try {
      String page = command.execute(request);
      if (page.startsWith("redirect:")) {
        String redirectUrl = page.substring("redirect:".length());
        response.sendRedirect(redirectUrl);
      } else {
        request.getRequestDispatcher(page).forward(request, response);
      }
    } catch (CommandException e) {
      request.setAttribute(RequestAttributeName.ERROR_MSG, e.getCause());
      request.getRequestDispatcher(PagePath.ERROR_500).forward(request, response);
    }
  }
}