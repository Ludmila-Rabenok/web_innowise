package by.rabenok.webinnowise.service;

import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.User;

import java.util.List;

public interface OrderService {
  void createOrder(String login, String[] procedures, String date, String time) throws ServiceException;

  List<Order> getOrdersFromUser(User user) throws ServiceException;

  List<Order> getAll() throws ServiceException;

  Order getById(int id) throws ServiceException;

  void approve(Order order) throws ServiceException;
}