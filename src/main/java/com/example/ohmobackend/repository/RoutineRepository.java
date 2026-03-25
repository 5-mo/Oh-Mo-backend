package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT r FROM Schedule s " +
            "JOIN s.routineList r " +
            "WHERE s.group = :group " +
            "AND r.date = :date")
    List<Routine> findRoutinesWithScheduleByGroupAndDate(
            @Param("group") Group group,
            @Param("date") LocalDate date
    );

    @Query("SELECT r FROM MemberCategory mc " +
            "JOIN mc.scheduleList s " +
            "JOIN s.routineList r " +
            "WHERE mc.member = :member " +
            "AND r.date BETWEEN :startDate AND :endDate")
    List<Routine> findRoutinesByMemberAndDate(
            @Param("member") Member member,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    void deleteAllBySchedule(Schedule schedule);

    List<Routine> findAllBySchedule(Schedule schedule);

    @Query("SELECT r FROM Schedule s " +
            "JOIN s.routineList r " +
            "WHERE s.group = :group " +
            "AND r.date = :date")
    List<Routine> findRoutinesWithScheduleAndAssignees(
            @Param("group") Group group,
            @Param("date") LocalDate date
    );

    @Query("SELECT r FROM Routine r " +
            "JOIN FETCH r.schedule s " +
            "JOIN FETCH s.group g " +
            "WHERE r.id = :id")
    public Optional<Routine> findWithScheduleAndGroupById(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Routine r WHERE r.id = :id")
    Optional<Routine> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT r FROM Routine r JOIN FETCH r.schedule s WHERE s.alarmTime = :alarmTime AND r.date = :date AND s.group IS NOT NULL")
    List<Routine> findGroupRoutinesWithAlarm(@Param("alarmTime") LocalTime alarmTime, @Param("date") LocalDate date);
}
