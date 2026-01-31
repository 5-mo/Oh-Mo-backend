package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query("SELECT r FROM MemberCategory mc " +
            "JOIN mc.scheduleList s " +
            "JOIN s.routineList r " +
            "WHERE mc.member = :member " +
            "AND r.date = :date")
    List<Routine> findRoutinesWithScheduleByMemberAndDate(
            @Param("member") Member member,
            @Param("date") LocalDate date
    );

    @Query("SELECT DISTINCT r FROM Routine r " +
            "JOIN FETCH r.schedule s " +
            "WHERE r.date = :date " +
            "AND s.group = :group")
    List<Routine> findRoutinesWithScheduleByGroupAndDate(
            @Param("group") Group group,
            @Param("date") LocalDate date
    );

    @Query("SELECT DISTINCT r FROM Routine r " +
            "JOIN FETCH r.schedule s " +
            "JOIN FETCH s.memberCategory mc " +
            "WHERE r.date BETWEEN :startDate AND :endDate " +
            "AND mc.member = :member")
    List<Routine> findRoutinesByMemberAndDate(
            @Param("member") Member member,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    void deleteAllBySchedule(Schedule schedule);

    List<Routine> findAllBySchedule(Schedule schedule);

    @Query("SELECT r FROM Routine r " +
            "JOIN FETCH r.schedule s " +
            "WHERE s.group = :group AND r.date = :date")
    List<Routine> findRoutinesWithScheduleAndAssignees(
            @Param("group") Group group,
            @Param("date") LocalDate date
    );

}
