package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Diary;
import com.example.ohmobackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    public Diary findByMemberAndDate(Member member, LocalDate date);
}
