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
@Table(
        name = "schedule",
        indexes = {
                @Index(name = "idx_schedule_date", columnList = "date"),
                @Index(name = "idx_schedule_member_category", columnList = "member_category_id"),
                @Index(name = "idx_schedule_type", columnList = "scheduleType"),
                @Index(name = "idx_schedule_date_category", columnList = "date, member_category_id")
        }
)
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
    @JoinColumn(name = "created_by")
    private Member member;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleAssignee> scheduleAssigneeList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Routine> routineList = new ArrayList<>();

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
    private Todo todo;

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

    public void updateAlarmTime(LocalTime time) {
        this.alarmTime = time;
    }
}
