package me.michalkunc.qbackend.utils;

import java.time.LocalDate;

public class DateUtils {
    public static LocalDate plusDayToToday(int dayCount) {
        return LocalDate.now().plusDays(dayCount);
    }
}
