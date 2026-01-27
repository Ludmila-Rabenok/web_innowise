package by.rabenok.webinnowise.service;

import by.rabenok.webinnowise.exception.ServiceException;
import by.rabenok.webinnowise.model.Order;

import java.util.List;

public interface OrderService {
  void createOrder(String userName, String[] procedures, String date, String time) throws ServiceException;

  List<Order> findOrdersByUserName(String userName) throws ServiceException;

  List<Order> findAll() throws ServiceException;

  Order findById(int id) throws ServiceException;

  boolean remove(int id) throws ServiceException;

  void approve(int id) throws ServiceException;

  void reject(int id) throws ServiceException;
}