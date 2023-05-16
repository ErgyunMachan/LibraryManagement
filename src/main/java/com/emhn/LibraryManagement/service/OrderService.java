package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.dto.OrderDto;
import com.emhn.LibraryManagement.model.Order;
import com.emhn.LibraryManagement.request.OrderRequest;

import java.util.List;

public interface OrderService {

  List<OrderDto> getAllOrders();

  List<OrderDto> getOrdersByClient(int id);

  Order addOrder(OrderRequest orderRequest);

  Order removeOrder(int id);

  OrderDto getOrderById(int id);

  boolean checkBookAvailability(BookDto book);

  void setQuantityAfterOrder(BookDto book, int bookID);

  List<OrderDto> getOrdersIssuedOnDate(String date);

  List<OrderDto> getOrdersIssuedAfterDate(String date);

  List<OrderDto> getOrdersIssuedBeforeDate(String date);

  List<OrderDto> getOrdersDueByDate(String date);

  List<OrderDto> getOrdersDueAfterDate(String date);

  List<OrderDto> getOrdersDueBeforeDate(String date);
}
