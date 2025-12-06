package com.example.ohmobackend.domain;

import com.example.ohmobackend.domain.common.BaseEntity;
import com.example.ohmobackend.domain.enums.GroupRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class MemberGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_group_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private GroupRole role;

    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "memberGroup", cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();

    @OneToMany(mappedBy = "memberGroup", cascade = CascadeType.ALL)
    private List<ScheduleAssignee> scheduleAssigneeList = new ArrayList<>();
}
