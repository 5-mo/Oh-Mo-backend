package com.example.ohmobackend.domain;

import com.example.ohmobackend.domain.common.BaseEntity;
import com.example.ohmobackend.domain.enums.ScheduleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    private LocalDate date;

    private LocalTime time;

    private boolean allowAlarm;

    private LocalTime alarmTime;

    private String content;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_category_id")
    private MemberCategory memberCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member member;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleAssignee> scheduleAssigneeList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Routine> routineList = new ArrayList<>();

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
    private Todo todo;

    public void updateDate(LocalDate date) {
        this.date = date;
    }

    public void updateAlarmTime(LocalTime time) {
        this.allowAlarm = true;
        this.alarmTime = time;
    }
}
