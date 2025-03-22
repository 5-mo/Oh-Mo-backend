package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.enums.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {

    public List<MemberCategory> findByMemberAndScheduleType(Member member, ScheduleType scheduleType);
}
