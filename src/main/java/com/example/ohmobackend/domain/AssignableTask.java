package com.example.ohmobackend.domain;

import java.time.LocalDate;
import java.util.List;

public interface AssignableTask {
    void updateStatus(boolean status);
    List<ScheduleAssignee> getAssignees();
    LocalDate getDate();
    Long getId();
}