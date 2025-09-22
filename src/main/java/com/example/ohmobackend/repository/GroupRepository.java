package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
