package com.example.ohmobackend.domain;

import com.example.ohmobackend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(
        name = "routine",
        indexes = {
                @Index(name = "idx_routine_date", columnList = "date"),
                @Index(name = "idx_routine_schedule", columnList = "schedule_id"),
                @Index(name = "idx_routine_date_schedule", columnList = "date, schedule_id")
        }
)
public class Routine extends BaseEntity implements AssignableTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long id;

    private LocalDate date;

    private boolean status;

    private DayOfWeek week;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<ScheduleAssignee> scheduleAssigneeList = new ArrayList<>();


    @Override
    public void updateStatus(boolean status) {
        this.status = status;
    }

    @Override
    public List<ScheduleAssignee> getAssignees() {
        return scheduleAssigneeList;
    }
}
