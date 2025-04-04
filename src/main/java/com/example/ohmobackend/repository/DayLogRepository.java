package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Daylog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayLogRepository extends JpaRepository<Daylog, Long> {
}
