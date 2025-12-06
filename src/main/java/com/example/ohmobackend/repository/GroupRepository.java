package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    public Optional<Group> findByGroupCode(String groupCode);
}
