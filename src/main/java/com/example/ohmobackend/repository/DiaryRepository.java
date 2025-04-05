package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
