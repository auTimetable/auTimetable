package ru.spbau.mit.auTimetable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by equi on 01.12.15.
 *
 * @author Kravchenko Dima
 */
public class Date {
    public String dayName;
    public String dayNameEn;
    public int day;
    public int month;
    public int year;
    public int dayOfWeek;

    private Calendar calendar;

    private final List<String> daysNames = Arrays.asList(
            "Воскресенье",
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота"
    );

    private final List<String> daysNamesEn = Arrays.asList(
            "sunday",
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday",
            "saturday"
    );

    public Date() {
        update();
    }

    public void update() {
        updateCalendar();
        updateDate();
    }

    public void updateCalendar() {
        calendar = Calendar.getInstance();
    }

    public void updateDate() {
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1; // +1 needs to be for 1..12
        year = calendar.get(Calendar.YEAR);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayName = daysNames.get(dayOfWeek - 1);
        dayNameEn = daysNamesEn.get(dayOfWeek - 1);
    }

    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        updateDate();
    }

    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        updateDate();
    }

    public int compare(Date date) {
        if (date.year < year)
            return -1;
        if (date.year > year)
            return 1;
        if (date.month < month)
            return -1;
        if (date.month > month)
            return 1;
        if (date.day < day)
            return -1;
        if (date.day > day)
            return 1;
        return 0;
    }
}
