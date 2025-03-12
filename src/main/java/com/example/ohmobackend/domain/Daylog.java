package com.example.ohmobackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Daylog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daylog_id")
    private Long id;

    private Date date;

    private String emoji;

    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member user;
}
