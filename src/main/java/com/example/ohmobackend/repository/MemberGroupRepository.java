package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
}
