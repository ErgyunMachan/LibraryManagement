package com.emhn.LibraryManagement.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateMapper {

  public LocalDate mapStringToLocalDate(String date) {
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(date, pattern);
  }
}
