package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND s.date = :date")
    public List<Schedule> findByMemberCategoryAndDate(MemberCategory memberCategory, LocalDate date);

    @Query("SELECT s FROM Schedule s WHERE s.memberCategory = :memberCategory AND FUNCTION('DATE_FORMAT', s.date, '%Y-%m') = :month")
    public List<Schedule> findByMemberCategoryAndMonth(MemberCategory memberCategory, String month);
}
