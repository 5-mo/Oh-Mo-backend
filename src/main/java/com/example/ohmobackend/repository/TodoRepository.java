package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
