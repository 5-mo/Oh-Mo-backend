package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {

    public Optional<MemberGroup> findByGroupAndMember(Group group, Member member);

    public boolean existsByGroupAndNickname(Group group, String nickname);

    long countByGroup(Group group);

    public List<MemberGroup> findAllByGroup(Group group);

    public List<MemberGroup> findAllByMember(Member member);

    public MemberGroup findByMemberAndGroup(Member member, Group group);

    @Query("""
            select mg
            from MemberGroup mg
            join fetch mg.group g
            join fetch mg.member m
            where mg.group in (
                select mg2.group
                from MemberGroup mg2
                where mg2.member = :member
            )
            and mg.role = 'MANAGER'
            """)
    List<MemberGroup> findHostMemberGroupsByMember(@Param("member") Member member);
}
