package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    public List<Question> findAllByMember(Member member);
}
