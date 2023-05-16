package com.emhn.LibraryManagement.repository;

import com.emhn.LibraryManagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
