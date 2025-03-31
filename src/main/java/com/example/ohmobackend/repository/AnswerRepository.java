package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Answer;
import com.example.ohmobackend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    public List<Answer> findAllByQuestion(Question question);
}
