package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    public List<Notice> findAllByDateAndGroup(LocalDate date, Group group);

    @Query("SELECT DISTINCT n FROM Notice n " +
            "WHERE n.date BETWEEN :startDate AND :endDate " +
            "AND n.group = :group ")
    List<Notice> findNoticesByGroupAndDate(
            @Param("group") Group group,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

