package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.GroupInvitation;
import com.example.ohmobackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Long> {
    List<GroupInvitation> findAllByInvitedMember(Member invitedMember);
    boolean existsByGroupAndInvitedMember(Group group, Member invitedMember);
}
