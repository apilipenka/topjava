package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalDateTime lt, LocalDate start, LocalDate end) {
        return lt.toLocalDate().compareTo(start == null ? LocalDate.MIN : start) >= 0 &&
                lt.toLocalDate().compareTo(end == null ? LocalDate.MAX : end) <= 0;
    }

    public static boolean isBetweenHalfOpen(LocalDateTime lt, LocalTime start, LocalTime end) {
        return lt.toLocalTime().compareTo(start == null ? LocalTime.MIN : start) >= 0 &&
                lt.toLocalTime().compareTo(end == null ? LocalTime.MAX : end) < 0;
    }

    public static <T extends TemporalAccessor> boolean isBetweenHalfOpen(LocalDateTime lt, T start, T end) {
        return lt.compareTo(ChronoLocalDateTime.from(start)) >= 0 &&
                lt.compareTo(ChronoLocalDateTime.from(end)) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

