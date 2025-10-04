package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query("SELECT DISTINCT r FROM Routine r " +
            "JOIN FETCH r.schedule s " +
            "JOIN s.memberCategory mc " +
            "WHERE r.date = :date " +
            "AND mc.member = :member")
    List<Routine> findRoutinesWithScheduleByMemberAndDate(
            @Param("member") Member member,
            @Param("date") LocalDate date
    );

}
