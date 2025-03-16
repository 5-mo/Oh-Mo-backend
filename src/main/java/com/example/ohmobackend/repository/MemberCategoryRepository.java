package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
}
