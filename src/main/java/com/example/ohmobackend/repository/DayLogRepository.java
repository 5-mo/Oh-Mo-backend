package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Daylog;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DayLogRepository extends JpaRepository<Daylog, Long> {

    public Daylog findByDateAndMember(LocalDate date, Member member);
}
