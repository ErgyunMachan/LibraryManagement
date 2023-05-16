package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.OrderDto;
import com.emhn.LibraryManagement.model.Order;
import com.emhn.LibraryManagement.request.OrderRequest;
import com.emhn.LibraryManagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/order")
  public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
    Order order = orderService.addOrder(orderRequest);
    URI location = UriComponentsBuilder.fromUriString("/orders/{id}")
                                       .buildAndExpand(order.getOrderID())
                                       .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping("/orders")
  public ResponseEntity<List<OrderDto>> getAllOrders() {
    List<OrderDto> orders = orderService.getAllOrders();

    return ResponseEntity.ok(orders);
  }

  @GetMapping("/order/{id}")
  public ResponseEntity<OrderDto> getOrderByID(@PathVariable int id) {
    OrderDto orderDto = orderService.getOrderById(id);

    return ResponseEntity.ok(orderDto);
  }

  @GetMapping("/client/{id}/orders")
  public ResponseEntity<List<OrderDto>> getOrderByClientID(@PathVariable int id) {
    List<OrderDto> ordersList = orderService.getOrdersByClient(id);

    return ResponseEntity.ok(ordersList);
  }

  @GetMapping("/orders/filterByIssueOnDate")
  public ResponseEntity<List<OrderDto>> getOrdersByIssuedOnDate(@RequestParam String date) {
    List<OrderDto> ordersList = orderService.getOrdersIssuedOnDate(date);

    return ResponseEntity.ok(ordersList);
  }

  @GetMapping("/orders/filterByIssueAfterDate")
  public ResponseEntity<List<OrderDto>> getOrdersByIssuedAfterDate(@RequestParam String date) {
    List<OrderDto> ordersList = orderService.getOrdersIssuedAfterDate(date);

    return ResponseEntity.ok(ordersList);
  }

  @GetMapping("/orders/filterByIssueBeforeDate")
  public ResponseEntity<List<OrderDto>> getOrdersByIssuedBeforeDate(@RequestParam String date) {
    List<OrderDto> ordersList = orderService.getOrdersIssuedBeforeDate(date);

    return ResponseEntity.ok(ordersList);
  }

  @GetMapping("/orders/filterByDueDate")
  public ResponseEntity<List<OrderDto>> getOrdersByDueDate(@RequestParam String date) {
    List<OrderDto> ordersList = orderService.getOrdersDueByDate(date);

    return ResponseEntity.ok(ordersList);
  }

  @GetMapping("/orders/filterByDueAfterDate")
  public ResponseEntity<List<OrderDto>> getOrdersByDueAfterDate(@RequestParam String date) {
    List<OrderDto> ordersList = orderService.getOrdersDueAfterDate(date);

    return ResponseEntity.ok(ordersList);
  }

  @GetMapping("/orders/filterByDueBeforeDate")
  public ResponseEntity<List<OrderDto>> getOrdersByDueBeforeDate(@RequestParam String date) {
    List<OrderDto> ordersList = orderService.getOrdersDueBeforeDate(date);

    return ResponseEntity.ok(ordersList);
  }

  @DeleteMapping("/order/{id}")
  public ResponseEntity<Order> deleteOrder(@PathVariable int id) {
    Order order = orderService.removeOrder(id);

    return ResponseEntity.ok(order);
  }
}
