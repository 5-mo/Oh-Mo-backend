package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND s.date = :date")
    public List<Schedule> findByMemberCategoryAndDate(
            @Param("memberCategory") MemberCategory memberCategory,
            @Param("date") LocalDate date
    );

    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND s.date BETWEEN :startDate AND :endDate")
    public List<Schedule> findByMemberCategoryAndMonth(
            @Param("memberCategory") MemberCategory memberCategory,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

//    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND s.date = :date AND s.status = true")
//    public List<Schedule> findByMemberCategoryAndDateAndStatusIsTrue(
//            @Param("memberCategory") MemberCategory memberCategory,
//            @Param("date") LocalDate date
//    );

    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND s.content LIKE %:keyword%")
    public List<Schedule> findByMemberCategoryAndTitleContaining(
            @Param("memberCategory") MemberCategory memberCategory,
            @Param("keyword") String keyword
    );

    public List<Schedule> findByContent(String content);

    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND s.date BETWEEN :startDate AND :endDate")
    List<Schedule> findByMemberCategoryAndDateBetween(
            @Param("memberCategory") MemberCategory memberCategory,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
