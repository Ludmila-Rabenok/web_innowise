package by.rabenok.webinnowise.command;

import javax.servlet.http.HttpServletRequest;


public interface Command {
  String execute(HttpServletRequest request);
//  default  void refresh(){}
}
