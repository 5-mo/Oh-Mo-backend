package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.ScheduleAssignee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleAssigneeRepository extends JpaRepository<ScheduleAssignee, Long> {
}
