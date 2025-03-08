package com.example.ohmobackend.domain;

import com.example.ohmobackend.domain.enums.ScheduleType;
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
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_category_id")
    private Long id;

    private String categoryName;

    private String color;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userCategory", cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();
}
