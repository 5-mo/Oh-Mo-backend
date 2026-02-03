package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
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

    @Query("SELECT DISTINCT t FROM Todo t " +
            "JOIN FETCH t.schedule s " +
            "WHERE s.group = :group AND s.date = :date")
    List<Todo> findTodoWithScheduleAndAssignees(
            @Param("group") Group group,
            @Param("date") LocalDate date);

    @Query("SELECT DISTINCT t FROM Todo t " +
            "JOIN FETCH t.schedule s " +
            "JOIN FETCH s.memberCategory mc " +
            "WHERE s.date = :date " +
            "AND mc.member = :member")
    List<Todo> findTodosWithScheduleByMemberAndDate(
            @Param("member") Member member,
            @Param("date") LocalDate date
    );

    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.schedule s " +
            "WHERE s.memberCategory = :memberCategory AND s.content LIKE %:keyword%")
    public List<Todo> findByMemberCategoryAndTitleContaining(
            @Param("memberCategory") MemberCategory memberCategory,
            @Param("keyword") String keyword
    );
}
