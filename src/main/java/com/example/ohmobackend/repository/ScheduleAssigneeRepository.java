package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.ScheduleAssignee;
import com.example.ohmobackend.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleAssigneeRepository extends JpaRepository<ScheduleAssignee, Long> {

    @Query("SELECT s FROM ScheduleAssignee s " +
            "JOIN FETCH s.memberGroup mg " +
            "JOIN FETCH mg.member " +
            "WHERE s.todo = :todo"
    )
    public List<ScheduleAssignee> findAllByTodo(Todo todo);

    @Query("SELECT s FROM ScheduleAssignee s " +
            "JOIN FETCH s.memberGroup mg " +
            "JOIN FETCH mg.member " +
            "WHERE s.routine = :routine")
    public List<ScheduleAssignee> findAllByRoutine(Routine routine);
}
