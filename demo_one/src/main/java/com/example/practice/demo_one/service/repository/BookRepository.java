package com.example.practice.demo_one.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.practice.demo_one.service.entity.Book;

public interface BookRepository extends JpaRepository<Book,Long> {

}
