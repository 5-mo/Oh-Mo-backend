package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    public Todo findBySchedule(Schedule schedule);

    @Query("SELECT DISTINCT t FROM Todo t " +
            "JOIN FETCH t.schedule s " +
            "JOIN FETCH s.memberCategory mc " +
            "WHERE t.status = :status " +
            "AND s.date = :date " +
            "AND mc.member = :member")
    List<Todo> findTodosWithScheduleByMemberAndDateAndStatus(
            @Param("member") Member member,
            @Param("date") LocalDate date,
            @Param("status") boolean status
    );
}
