package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.enums.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {

    public List<MemberCategory> findByMemberAndScheduleType(Member member, ScheduleType scheduleType);
    public List<MemberCategory> findByMember(Member member);

    public Optional<MemberCategory> findByMemberAndCategoryNameAndScheduleType(Member member,
                                                                                 String categoryName,
                                                                                 ScheduleType scheduleType);
}
