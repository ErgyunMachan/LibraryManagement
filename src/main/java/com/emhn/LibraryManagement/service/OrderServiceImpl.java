package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.dto.OrderDto;
import com.emhn.LibraryManagement.exception.OrderCreationNotAllowedException;
import com.emhn.LibraryManagement.exception.OrderNotFoundException;
import com.emhn.LibraryManagement.mapper.DateMapper;
import com.emhn.LibraryManagement.mapper.OrderMapper;
import com.emhn.LibraryManagement.model.Book;
import com.emhn.LibraryManagement.model.Order;
import com.emhn.LibraryManagement.repository.OrderRepository;
import com.emhn.LibraryManagement.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ClientService clientService;
  private final BookService bookService;
  private final DateMapper dateMapper;

  private final OrderMapper orderMapper;

  @Autowired
  public OrderServiceImpl(
    OrderRepository orderRepository, ClientService clientService, BookService bookService, DateMapper dateMapper,
    OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.clientService = clientService;
    this.bookService = bookService;
    this.dateMapper = dateMapper;
    this.orderMapper = orderMapper;
  }

  @Override
  public List<OrderDto> getAllOrders() {
    List<Order> ordersList = orderRepository.findAll();
    return orderMapper.mapOrdersToDtosList(ordersList);
  }

  @Override
  public List<OrderDto> getOrdersByClient(int id) {
    List<OrderDto> orders = getAllOrders();
    ClientDto client = clientService.getClientByID(id);

    return orders.stream().filter(order -> order.getClientName().equals(client.getClientName())).collect(
      Collectors.toList());
  }

  @Override
  public Order addOrder(OrderRequest orderRequest) {
    ClientDto client = clientService.getClientByID(orderRequest.getClientID());
    BookDto book = bookService.getBookById(orderRequest.getBookID());
    LocalDate issueDate = LocalDate.now();
    LocalDate dueDate = LocalDate.now().plusMonths(1);
    if (checkBookAvailability(book)) {
      setQuantityAfterOrder(book, orderRequest.getBookID());
    } else {
      throw new OrderCreationNotAllowedException("Order is out of stock!!");
    }
    Order order = new Order(client.getClientName(), book.getBookName(), issueDate, dueDate);
    setQuantityAfterOrder(book, orderRequest.getBookID());

    return orderRepository.save(order);
  }

  public void setQuantityAfterOrder(BookDto book, int bookID) {

    book.setQuantity(book.getQuantity() - 1);
    Book updatedBook =
      new Book(bookID, book.getBookName(), book.getAuthorName(), book.getPublishedDate(), book.getQuantity());
    bookService.updateQuantity(updatedBook);
  }

  @Override
  public boolean checkBookAvailability(BookDto book) {
    boolean isAvailable;
    isAvailable = book.getQuantity() != 0;

    return isAvailable;
  }

  @Override
  public Order removeOrder(int id) {
    Order orderToRemove;

    Optional<Order> order = orderRepository.findById(id);

    if (order.isPresent()) {
      orderToRemove = order.get();
    } else {
      throw new OrderNotFoundException("Order is not found!!");
    }

    orderRepository.delete(orderToRemove);
    return orderToRemove;
  }

  @Override
  public OrderDto getOrderById(int id) {
    Order order;
    Optional<Order> findOrder = orderRepository.findById(id);
    if (findOrder.isPresent()) {
      order = findOrder.get();
    } else {
      throw new OrderNotFoundException("Order not found!!");
    }

    return orderMapper.mapOrderToDto(order);
  }

  @Override
  public List<OrderDto> getOrdersIssuedOnDate(String date) {
    LocalDate stringToLocalDate = dateMapper.mapStringToLocalDate(date);
    List<OrderDto> orders = getAllOrders();
    List<OrderDto> orderByIssuedDate =
      orders.stream().filter(order -> order.getIssueDate().isEqual(stringToLocalDate)).collect(Collectors.toList());

    if (orderByIssuedDate.isEmpty()) {
      throw new OrderNotFoundException("Order is not found!!");
    } else {
      return orderByIssuedDate;
    }
  }

  @Override
  public List<OrderDto> getOrdersIssuedAfterDate(String date) {
    LocalDate strToLocalDate = dateMapper.mapStringToLocalDate(date);
    List<OrderDto> orders = getAllOrders();
    List<OrderDto> orderByIssuedAfter =
      orders.stream().filter(order -> order.getIssueDate().isAfter(strToLocalDate)).collect(Collectors.toList());

    if (orderByIssuedAfter.isEmpty()) {
      throw new OrderNotFoundException("Order is not found!!");
    } else {
      return orderByIssuedAfter;
    }
  }

  @Override
  public List<OrderDto> getOrdersIssuedBeforeDate(String date) {
    LocalDate convertStrToLocalDate = dateMapper.mapStringToLocalDate(date);
    List<OrderDto> orders = getAllOrders();

    List<OrderDto> orderByIssuedBefore =
      orders.stream().filter(order -> order.getIssueDate().isBefore(convertStrToLocalDate))
            .collect(Collectors.toList());

    if (orderByIssuedBefore.isEmpty()) {
      throw new OrderNotFoundException("Order is not found!!");
    } else {
      return orderByIssuedBefore;
    }
  }

  @Override
  public List<OrderDto> getOrdersDueByDate(String date) {
    LocalDate strToLocalDate = dateMapper.mapStringToLocalDate(date);
    List<OrderDto> orders = getAllOrders();

    List<OrderDto> orderByDueDate =
      orders.stream().filter(order -> order.getDueDate().isEqual(strToLocalDate)).collect(Collectors.toList());
    if (orderByDueDate.isEmpty()) {
      throw new OrderNotFoundException("Order is not found!!");
    } else {
      return orderByDueDate;
    }
  }

  @Override
  public List<OrderDto> getOrdersDueAfterDate(String date) {
    LocalDate strToLocalDate = dateMapper.mapStringToLocalDate(date);
    List<OrderDto> orders = getAllOrders();

    List<OrderDto> orderByDueAfter =
      orders.stream().filter(order -> order.getDueDate().isAfter(strToLocalDate)).collect(Collectors.toList());
    if (orderByDueAfter.isEmpty()) {
      throw new OrderNotFoundException("Order is not found!!");
    } else {
      return orderByDueAfter;
    }
  }

  @Override
  public List<OrderDto> getOrdersDueBeforeDate(String date) {
    LocalDate strToLocalDate = dateMapper.mapStringToLocalDate(date);
    List<OrderDto> orders = getAllOrders();

    List<OrderDto> orderByDueBefore =
      orders.stream().filter(order -> order.getDueDate().isBefore(strToLocalDate)).collect(Collectors.toList());
    if (orderByDueBefore.isEmpty()) {
      throw new OrderNotFoundException("Order is not found!!");
    } else {
      return orderByDueBefore;
    }
  }
}
