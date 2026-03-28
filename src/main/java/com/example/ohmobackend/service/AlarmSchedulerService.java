package com.example.ohmobackend.service;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.domain.enums.FcmNotificationType;
import com.example.ohmobackend.repository.RoutineRepository;
import com.example.ohmobackend.repository.ScheduleAssigneeRepository;
import com.example.ohmobackend.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmSchedulerService {

    private final TodoRepository todoRepository;
    private final RoutineRepository routineRepository;
    private final ScheduleAssigneeRepository scheduleAssigneeRepository;
    private final FcmService fcmService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    public void sendAlarmNotifications() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        LocalDate today = LocalDate.now();

        List<Todo> todos = todoRepository.findGroupTodosWithAlarm(now, today);
        for (Todo todo : todos) {
            String content = todo.getSchedule().getContent();
            List<String> tokens = scheduleAssigneeRepository.findAllByTodo(todo).stream()
                    .map(sa -> sa.getMemberGroup().getMember().getFcmToken())
                    .toList();
            fcmService.sendNotifications(tokens, "일정 알림 ⏰", content + " 일정이 예정되어 있습니다.", FcmNotificationType.SCHEDULE_ALARM);
        }

        List<Routine> routines = routineRepository.findGroupRoutinesWithAlarm(now, today);
        for (Routine routine : routines) {
            String content = routine.getSchedule().getContent();
            List<String> tokens = scheduleAssigneeRepository.findAllByRoutine(routine).stream()
                    .map(sa -> sa.getMemberGroup().getMember().getFcmToken())
                    .toList();
            fcmService.sendNotifications(tokens, "일정 알림 ⏰", content + " 일정이 예정되어 있습니다.", FcmNotificationType.SCHEDULE_ALARM);
        }
    }
}
