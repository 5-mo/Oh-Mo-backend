package com.example.ohmobackend.service.scheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateCalculator {

    public static List<LocalDate> getDates(LocalDate startDate, LocalDate endDate, List<DayOfWeek> weeks) {
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

    public static List<LocalDate> getDatesFromNowDate(LocalDate endDate, List<DayOfWeek> weeks) {
        LocalDate startDate = LocalDate.now();  // 시작 날짜 (오늘)
        return getDates(startDate, endDate, weeks);
    }
}
