package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.ScheduleAssignee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleAssigneeRepository extends JpaRepository<ScheduleAssignee, Long> {

    public List<ScheduleAssignee> findAllBySchedule(Schedule schedule);
}
