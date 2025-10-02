package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {

    public Optional<MemberGroup> findByGroupAndMember(Group group, Member member);
}
