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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JoinColumn(name = "member_group_id")
    private MemberGroup createdBy;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Routine> routineList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "schedule_repeat_days",
            joinColumns = @JoinColumn(name = "schedule_id")
    )

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> repeatWeek = new HashSet<>();

    public void updateDate(LocalDate date) {
        this.date = date;
    }

    public void updateTime(LocalTime time) {
        this.time = time;
    }

    public void updateAlarmTime(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateRepeatWeek(Set<DayOfWeek> repeatWeek) {
        this.repeatWeek.clear();

        if (repeatWeek != null && !repeatWeek.isEmpty()) {
            this.repeatWeek.addAll(repeatWeek);
        }
    }

    public void changeMemberCategory(MemberCategory memberCategory) {
        this.memberCategory = memberCategory;
    }
}
