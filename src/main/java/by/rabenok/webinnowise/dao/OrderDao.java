package by.rabenok.webinnowise.dao;

import by.rabenok.webinnowise.exception.DaoException;
import by.rabenok.webinnowise.model.Order;
import by.rabenok.webinnowise.model.User;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
  List<Order> findOrdersFromUser(User user) throws DaoException;

  void save(Order order) throws DaoException;

  void update(Order order) throws DaoException;

  List<Order> findAll() throws DaoException;

  Optional<Order> findById(int id) throws DaoException;
}