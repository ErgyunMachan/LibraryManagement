package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.OrderDto;
import com.emhn.LibraryManagement.model.Order;
import com.emhn.LibraryManagement.request.OrderRequest;
import com.emhn.LibraryManagement.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
  @Mock
  OrderService orderService;


  @Mock
  private MockMvc mvc;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders
      .standaloneSetup(new OrderController(orderService))
      .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
      .build();
  }

  @Test
  public void testCreateOrder() throws Exception{
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);

    when(orderService.addOrder(any())).thenReturn(
      new Order(1,"Bill Johnson", "harry potter", issueDate,dueDate));
    String json =
      "{\"clientID\" : 1 , \"bookID\" : 1 }";


    mvc.perform(MockMvcRequestBuilders
                  .post("/order")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json))
       .andExpect(status().isCreated())
       .andExpect(MockMvcResultMatchers.header().exists("Location"))
       .andExpect(MockMvcResultMatchers.header().string("Location", "/orders/1"));

    verify(orderService).addOrder(any(OrderRequest.class));
  }

  @Test
  public void testGetAllOrders() throws Exception{
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    when(orderService.getAllOrders()).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/orders"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));

  }

  @Test
  public void testGetOrderByID() throws Exception {
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    when(orderService.getOrderById(1)).thenReturn(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate));
    mvc.perform(MockMvcRequestBuilders.get("/order/{id}", 1))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Bill Johnson"));
  }

  @Test
  public void testGetOrderByClientID() throws Exception {
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    when(orderService.getOrdersByClient(1)).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/client/{id}/orders", 1))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));
  }

  @Test
  public void testGetOrdersByIssuedOnDate() throws Exception{
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "19/05/2010";

    when(orderService.getOrdersIssuedOnDate(date)).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/orders/filterByIssueOnDate")
                                      .param("date", "19/05/2010"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));

  }

  @Test
  public void testGetOrdersByIssuedAfterDate() throws Exception{
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "18/05/2010";

    when(orderService.getOrdersIssuedAfterDate(date)).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/orders/filterByIssueAfterDate")
                                      .param("date", "18/05/2010"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));
  }

  @Test
  public void testGetOrdersByIssuedBeforeDate() throws Exception {
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "20/05/2010";

    when(orderService.getOrdersIssuedBeforeDate(date)).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/orders/filterByIssueBeforeDate")
                                      .param("date", "20/05/2010"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));
  }

  @Test
  public void testGetOrdersByDueDate() throws Exception {
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "19/06/2010";

    when(orderService.getOrdersDueByDate(date)).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/orders/filterByDueDate")
                                      .param("date", "19/06/2010"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));
  }

  @Test
  public void testGetOrdersByDueAfterDate() throws Exception {
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "18/06/2010";

    when(orderService.getOrdersDueAfterDate(date)).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/orders/filterByDueAfterDate")
                                      .param("date", "18/06/2010"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));
  }

  @Test
  public void testGetOrdersByDueBeforeDate() throws Exception {
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    String date = "20/06/2010";

    when(orderService.getOrdersDueBeforeDate(date)).thenReturn(Collections.singletonList(new OrderDto("Bill Johnson","harry potter",issueDate,dueDate)));
    mvc.perform(MockMvcRequestBuilders.get("/orders/filterByDueBeforeDate")
                                      .param("date", "20/06/2010"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Bill Johnson"));
  }

  @Test
  public void testDeleteOrder() throws Exception {
    LocalDate issueDate = LocalDate.of(2010, 5, 19);
    LocalDate dueDate = issueDate.plusMonths(1);
    when(orderService.removeOrder(1)).thenReturn(new Order(1,"Bill Johnson","harry potter",issueDate,dueDate));
    mvc.perform(MockMvcRequestBuilders.delete("/order/{id}", 1))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Bill Johnson"));

  }
}