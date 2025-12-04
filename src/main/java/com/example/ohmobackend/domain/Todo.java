package com.example.ohmobackend.domain;


import com.example.ohmobackend.domain.common.BaseEntity;
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
@Table(name = "todo")
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    private boolean status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    private List<ScheduleAssignee> scheduleAssigneeList = new ArrayList<>();

    public void updateStatus() {
        this.status = !this.status;
    }
}
