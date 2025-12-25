package by.rabenok.webinnowise.controller.filter;


import by.rabenok.webinnowise.controller.RequestAttributeName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class LoggingRequestFilter implements Filter {
  private Logger logger;

  @Override
  public void init(FilterConfig filterConfig) {
    logger = LogManager.getLogger();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    String method = ((HttpServletRequest) request).getMethod();
    String path = ((HttpServletRequest) request).getRequestURI();
    String queryString = ((HttpServletRequest) request).getQueryString();
    String user = (String) ((HttpServletRequest) request).getSession().getAttribute(RequestAttributeName.USER);
    String role = (String) ((HttpServletRequest) request).getSession().getAttribute(RequestAttributeName.ROLE);
    logger.info("Method : {}, request path : {}, parameters : {}, user : {}, role : {}",
            method, path, queryString, user, role);
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
}