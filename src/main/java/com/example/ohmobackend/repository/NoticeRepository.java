package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    public List<Notice> findAllByDateAndGroup(LocalDate date, Group group);
}

