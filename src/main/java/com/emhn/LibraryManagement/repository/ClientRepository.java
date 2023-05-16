package com.emhn.LibraryManagement.repository;

import com.emhn.LibraryManagement.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
