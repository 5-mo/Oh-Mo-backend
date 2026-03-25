package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    public Optional<Group> findByGroupCode(String groupCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM Group g WHERE g.id = :id")
    Optional<Group> findByIdWithLock(@Param("id") Long id);
}
