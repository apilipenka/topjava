package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static ru.javawebinar.topjava.util.TimeUtil.readableDateTimeFormatter;

public class Meal {

    public Integer id;

    private LocalDateTime dateTime;

    private String description;

    private Integer calories;

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTimeInReadableFormat() {
        return readableDateTimeFormatter.format(dateTime);
    }

    public Date localDateTimeToDate() {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


}
