package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
