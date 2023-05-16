package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.dto.OrderDto;
import com.emhn.LibraryManagement.exception.ClientNotFoundException;
import com.emhn.LibraryManagement.exception.OrderCreationNotAllowedException;
import com.emhn.LibraryManagement.exception.OrderNotFoundException;
import com.emhn.LibraryManagement.mapper.DateMapper;
import com.emhn.LibraryManagement.mapper.OrderMapper;
import com.emhn.LibraryManagement.model.Book;
import com.emhn.LibraryManagement.model.Order;
import com.emhn.LibraryManagement.repository.OrderRepository;
import com.emhn.LibraryManagement.request.OrderRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();
  @Mock
  OrderRepository orderRepository;
  @Mock
  ClientService clientService;
  @Mock
  BookService bookService;
  @Mock
  DateMapper dateMapper;
  @Mock
  OrderMapper orderMapper;
  @InjectMocks
  OrderServiceImpl orderService;


  @Test
  public void testGetAllOrders() {
    LocalDate issueDate = LocalDate.now();
    LocalDate dueDate = issueDate.plusMonths(1);
    List<Order> orderList = new ArrayList<>();
    orderList.add(new Order(1,"Bill Jackson","harry potter",issueDate,dueDate));
    when(orderRepository.findAll()).thenReturn(orderList);
    when(orderMapper.mapOrdersToDtosList(orderList)).thenReturn(Collections.singletonList(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate)));
    List<OrderDto> orderDtoList = orderService.getAllOrders();
    Assert.assertEquals("Bill Jackson",orderDtoList.get(0).getClientName());

  }

  @Test
  public void testGetOrdersByClient() {
    LocalDate issueDate = LocalDate.now();
    LocalDate dueDate = issueDate.plusMonths(1);
    List<OrderDto> orderDtoList = new ArrayList<>();
    orderDtoList.add(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    ClientDto clientDto = new ClientDto("Bill Jackson");
    when(orderService.getAllOrders()).thenReturn(orderDtoList);
    when(clientService.getClientByID(1)).thenReturn(clientDto);
    List<OrderDto> expectedOrdersByClientList =
      orderDtoList.stream().filter(order -> order.getClientName().equals(clientDto.getClientName())).toList();
    List<OrderDto> ordersByClient = orderService.getOrdersByClient(1);
    Assert.assertSame(expectedOrdersByClientList.get(0).getClientName(),ordersByClient.get(0).getClientName());
  }

  @Test
  public void testGetOrdersByClient_Throws_ClientNotFound_Exception(){
    int id = 1;
    exception.expect(ClientNotFoundException.class);
    exception.expectMessage("Client is not exist with id: " + id);
    when(clientService.getClientByID(1)).thenThrow(new ClientNotFoundException("Client is not exist with id: " + id));
    orderService.getOrdersByClient(1);
  }


  @Test
  public void testAddOrder_Success() {
    LocalDate issueDate = LocalDate.now();
    LocalDate dueDate = issueDate.plusMonths(1);
    OrderRequest orderRequest = new OrderRequest(1,1);
    BookDto bookDto = new BookDto("harry potter","J. K. Rowling",
                                  LocalDate.of(1990,2,19),5);
    when(clientService.getClientByID(orderRequest.getClientID())).thenReturn(new ClientDto("Bill John"));
    when(bookService.getBookById(orderRequest.getBookID())).thenReturn(new BookDto("harry potter","J. K. Rowling",
                                                            LocalDate.of(1990,2,19),5));
    Order createOrder = new Order("Bill John","harry potter",issueDate,dueDate);
    when(orderRepository.save(any())).thenReturn(new Order("Bill John","harry potter",issueDate,dueDate));
    Order createdOrder = orderService.addOrder(orderRequest);
    Assert.assertSame("Bill John",createdOrder.getClientName());
    Assert.assertTrue(orderService.checkBookAvailability(bookDto));
  }

  @Test
  public void testAddOrder_Failed_throwsOrderCreationNotAllowed_Exception(){
    exception.expect(OrderCreationNotAllowedException.class);
    exception.expectMessage("Order is out of stock!!");
    LocalDate issueDate = LocalDate.now();
    LocalDate dueDate = issueDate.plusMonths(1);
    OrderRequest orderRequest = new OrderRequest(1,1);
    when(clientService.getClientByID(orderRequest.getClientID())).thenReturn(new ClientDto("Bill John"));
    when(bookService.getBookById(orderRequest.getBookID())).thenReturn(new BookDto("harry potter","J. K. Rowling",
                                                                                   LocalDate.of(1990,2,19),0));
    orderService.addOrder(orderRequest);

  }

  @Test
  public void testSetQuantityAfterOrder() {
    BookDto oldBook = new BookDto("harry potter","J. K. Rowling",
                                  LocalDate.of(1990,2,19),5);

     oldBook.setQuantity(4);
     Book newBook = new Book(1,oldBook.getBookName(),oldBook.getAuthorName(),oldBook.getPublishedDate(),
                                oldBook.getQuantity());
    orderService.setQuantityAfterOrder(oldBook,1);
    Assert.assertEquals(4,newBook.getQuantity());

  }

  @Test
  public void testCheckBookAvailability_True(){
    BookDto bookDto = new BookDto("harry potter","J. K. Rowling",
                                  LocalDate.of(1990,2,19),1);
    boolean bookIsAvailable = orderService.checkBookAvailability(bookDto);
    Assert.assertTrue(bookIsAvailable);
  }

  @Test
  public void testCheckBookAvailability_False(){
    BookDto bookDto = new BookDto("harry potter","J. K. Rowling",
                                  LocalDate.of(1990,2,19),0);
    boolean bookIsAvailable = orderService.checkBookAvailability(bookDto);
    Assert.assertFalse(bookIsAvailable);
  }


  @Test
  public void testRemoveOrder() {
    LocalDate issueDate = LocalDate.of(2011,1,12);
    LocalDate dueDate = issueDate.plusMonths(1);
    Order orderToRemove = new Order(1,"Bill John","harry potter",issueDate,dueDate);
    Optional<Order> optionalOrder = Optional.of(orderToRemove);
    when(orderRepository.findById(1)).thenReturn(optionalOrder);
    Order removedOrder = orderService.removeOrder(1);
    Assert.assertSame(removedOrder,orderToRemove);
  }

  @Test
  public void testRemoveOrder_Throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    when(orderRepository.findById(anyInt())).thenThrow(new OrderNotFoundException("Order is not found!!"));
    orderService.removeOrder(anyInt());
  }

  @Test
  public void testGetOrderById() {
    LocalDate issueDate = LocalDate.of(2011,1,12);
    LocalDate dueDate = issueDate.plusMonths(1);
    Order order = new Order(1,"Bill Jackson","harry potter",issueDate,dueDate);
    Optional<Order> optionalOrder = Optional.of(order);
    when(orderRepository.findById(1)).thenReturn(optionalOrder);
    when(orderMapper.mapOrderToDto(order)).thenReturn(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    OrderDto orderDto = orderService.getOrderById(1);
    Assert.assertEquals("Bill Jackson",orderDto.getClientName());
  }

  @Test
  public void testGetOrderById_throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    when(orderRepository.findById(anyInt())).thenThrow(new OrderNotFoundException("Order is not found!!"));
    orderService.getOrderById(anyInt());
  }

  @Test
  public void testGetOrdersIssuedOnDate() {
    LocalDate issueDate = LocalDate.of(2009,11,1);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "01/11/2009";
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate convertedDate = LocalDate.parse(date, pattern);
    List<OrderDto> orderList = new ArrayList<>();
    orderList.add( new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    when(dateMapper.mapStringToLocalDate(date)).thenReturn(convertedDate);
    when(orderService.getAllOrders()).thenReturn(Collections.singletonList(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate)));
    List<OrderDto> expectedOrderList =
      orderList.stream().filter(order -> order.getIssueDate().isEqual(convertedDate)).toList();
    List<OrderDto> orderListFromMethodCall = orderService.getOrdersIssuedOnDate(date);
    Assert.assertEquals(expectedOrderList.get(0).getClientName(),orderListFromMethodCall.get(0).getClientName());
  }

  @Test
  public void testGetOrdersIssuedOnDate_Throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    orderService.getOrdersIssuedOnDate(anyString());
  }

  @Test
  public void testGetOrdersIssuedAfterDate() {
    LocalDate issueDate = LocalDate.of(2009,11,1);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "01/10/2009";
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate convertedDate = LocalDate.parse(date, pattern);
    List<OrderDto> orderList = new ArrayList<>();
    orderList.add( new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    when(dateMapper.mapStringToLocalDate(date)).thenReturn(convertedDate);
    when(orderService.getAllOrders()).thenReturn(Collections.singletonList(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate)));
    List<OrderDto> expectedOrderList =
      orderList.stream().filter(order -> order.getIssueDate().isAfter(convertedDate)).toList();
    List<OrderDto> orderListFromMethodCall = orderService.getOrdersIssuedAfterDate(date);
    Assert.assertEquals(expectedOrderList.get(0).getClientName(),orderListFromMethodCall.get(0).getClientName());
  }

  @Test
  public void testGetOrdersIssuedAfterDate_Throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    orderService.getOrdersIssuedAfterDate(anyString());
  }

  @Test
  public void testGetOrdersIssuedBeforeDate() {
    LocalDate issueDate = LocalDate.of(2009,11,1);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "03/11/2009";
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate convertedDate = LocalDate.parse(date, pattern);
    List<OrderDto> orderList = new ArrayList<>();
    orderList.add( new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    when(dateMapper.mapStringToLocalDate(date)).thenReturn(convertedDate);
    when(orderService.getAllOrders()).thenReturn(Collections.singletonList(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate)));
    List<OrderDto> expectedOrderList =
      orderList.stream().filter(order -> order.getIssueDate().isBefore(convertedDate)).toList();
    List<OrderDto> orderListFromMethodCall = orderService.getOrdersIssuedBeforeDate(date);
    Assert.assertEquals(expectedOrderList.get(0).getClientName(),orderListFromMethodCall.get(0).getClientName());
  }

  @Test
  public void testGetOrdersIssuedBeforeDate_Throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    orderService.getOrdersIssuedBeforeDate(anyString());
  }

  @Test
  public void testGetOrdersDueByDate() {
    LocalDate issueDate = LocalDate.of(2009,11,1);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "01/12/2009";
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate convertedDate = LocalDate.parse(date, pattern);
    List<OrderDto> orderList = new ArrayList<>();
    orderList.add( new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    when(dateMapper.mapStringToLocalDate(date)).thenReturn(convertedDate);
    when(orderService.getAllOrders()).thenReturn(Collections.singletonList(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate)));
    List<OrderDto> expectedOrderList =
      orderList.stream().filter(order -> order.getDueDate().isEqual(convertedDate)).toList();
    List<OrderDto> orderListFromMethodCall = orderService.getOrdersDueByDate(date);
    Assert.assertEquals(expectedOrderList.get(0).getClientName(),orderListFromMethodCall.get(0).getClientName());
  }

  @Test
  public void testGetOrdersDueByDate_Throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    orderService.getOrdersDueByDate(anyString());
  }

  @Test
  public void testGetOrdersDueAfterDate() {
    LocalDate issueDate = LocalDate.of(2009,11,1);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "30/11/2009";
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate convertedDate = LocalDate.parse(date, pattern);
    List<OrderDto> orderList = new ArrayList<>();
    orderList.add( new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    when(dateMapper.mapStringToLocalDate(date)).thenReturn(convertedDate);
    when(orderService.getAllOrders()).thenReturn(Collections.singletonList(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate)));
    List<OrderDto> expectedOrderList =
      orderList.stream().filter(order -> order.getDueDate().isAfter(convertedDate)).toList();
    List<OrderDto> orderListFromMethodCall = orderService.getOrdersDueAfterDate(date);
    Assert.assertEquals(expectedOrderList.get(0).getClientName(),orderListFromMethodCall.get(0).getClientName());
  }

  @Test
  public void testGetOrdersDueAfterDate_Throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    orderService.getOrdersDueAfterDate(anyString());
  }

  @Test
  public void testGetOrdersDueBeforeDate() {
    LocalDate issueDate = LocalDate.of(2009,11,1);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "02/12/2009";
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate convertedDate = LocalDate.parse(date, pattern);
    List<OrderDto> orderList = new ArrayList<>();
    orderList.add( new OrderDto("Bill Jackson","harry potter",issueDate,dueDate));
    when(dateMapper.mapStringToLocalDate(date)).thenReturn(convertedDate);
    when(orderService.getAllOrders()).thenReturn(Collections.singletonList(new OrderDto("Bill Jackson","harry potter",issueDate,dueDate)));
    List<OrderDto> expectedOrderList =
      orderList.stream().filter(order -> order.getDueDate().isBefore(convertedDate)).toList();
    List<OrderDto> orderListFromMethodCall = orderService.getOrdersDueBeforeDate(date);
    Assert.assertEquals(expectedOrderList.get(0).getClientName(),orderListFromMethodCall.get(0).getClientName());
  }

  @Test
  public void testGetOrdersDueBeforeDate_Throws_OrderNotFound_Exception(){
    exception.expect(OrderNotFoundException.class);
    exception.expectMessage("Order is not found!!");
    orderService.getOrdersDueBeforeDate(anyString());
  }
}