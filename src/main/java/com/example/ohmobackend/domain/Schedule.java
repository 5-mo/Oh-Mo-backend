package com.example.ohmobackend.domain;

import com.example.ohmobackend.domain.enums.ScheduleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    private LocalDate date;

    private LocalTime time;

    private boolean alarm;

    private String content;

    private boolean status;

    private LocalDate routineEndDate;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_category_id")
    private MemberCategory memberCategory;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<RoutineWeek> routineWeekList = new ArrayList<>();

}
