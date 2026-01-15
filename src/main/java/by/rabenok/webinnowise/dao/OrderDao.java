package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
  List<Order> findOrdersByUserName(String uerName) throws DaoException;

  void save(Order order, String[] procedures) throws DaoException;

  void updateStatusAndBill(Order order) throws DaoException;

  List<Order> findAll() throws DaoException;

  Optional<Order> findById(int id) throws DaoException;
}