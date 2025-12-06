package com.example.ohmobackend.domain;

import java.util.List;

public interface AssignableTask {
    void updateStatus(boolean status);
    List<ScheduleAssignee> getAssignees();
}