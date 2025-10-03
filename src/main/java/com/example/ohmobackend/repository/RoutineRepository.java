package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
