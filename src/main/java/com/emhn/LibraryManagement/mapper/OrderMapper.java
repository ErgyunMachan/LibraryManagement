package com.emhn.LibraryManagement.mapper;

import com.emhn.LibraryManagement.dto.OrderDto;
import com.emhn.LibraryManagement.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

  public List<OrderDto> mapOrdersToDtosList(List<Order> orders) {
    List<OrderDto> orderDtos = new ArrayList<>();
    for (Order order : orders) {
      OrderDto orderDto =
        new OrderDto(order.getClientName(), order.getBookName(), order.getIssueDate(), order.getDueDate());
      orderDtos.add(orderDto);
    }

    return orderDtos;
  }

  public OrderDto mapOrderToDto(Order order) {

    return new OrderDto(order.getClientName(), order.getBookName(), order.getIssueDate(), order.getDueDate());
  }
}
