package com.example.ohmobackend.service.scheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DateCalculator {

    public static List<LocalDate> getDates(LocalDate startDate, LocalDate endDate, Set<DayOfWeek> weeks) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (weeks.contains(dayOfWeek)) {
                dates.add(currentDate);
            }
            currentDate = currentDate.plusDays(1); // 하루씩 증가
        }

        return dates;
    }

    public static List<LocalDate> getDatesFromRepeatWeeks(LocalDate startDate, LocalDate endDate, Set<DayOfWeek> weeks) {
        return getDates(startDate, endDate, weeks);
    }
}
