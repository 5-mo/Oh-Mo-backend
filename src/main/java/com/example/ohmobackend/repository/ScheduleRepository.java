package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.enums.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.member = :member AND s.date = :date AND s.scheduleType = :scheduleType")
    public List<Schedule> findByMemberAndDateAndScheduleType(
            @Param("member") Member member,
            @Param("date") LocalDate date,
            @Param("scheduleType") ScheduleType scheduleType
    );

    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND FUNCTION('DATE_FORMAT', s.date, '%Y-%m') = :month")
    public List<Schedule> findByMemberCategoryAndMonth(MemberCategory memberCategory, String month);

    @Query("SELECT s FROM Schedule s WHERE s.member = :member AND s.date = :date AND s.scheduleType = :scheduleType AND s.status = true")
    public List<Schedule> findByMemberAndDateAndScheduleTypeAndStatusIsTrue(
            @Param("member") Member member,
            @Param("date") LocalDate date,
            @Param("scheduleType") ScheduleType scheduleType
    );
}
