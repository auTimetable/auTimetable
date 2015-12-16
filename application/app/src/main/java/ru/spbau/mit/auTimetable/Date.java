package ru.spbau.mit.auTimetable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//TODO totally get rid of Calendar and java.util.Date and use Joda Time
public class Date {
    public String dayName;
    public String dayNameEn;
    public int dayOfWeek;

    public int day;
    public int month;
    public int year; // this 3 fields for convenience.

    public Calendar calendar;

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

    public Date(int year, int month, int day) {
        updateCalendar();
        calendar.set(year, month, day, 0, 0);
        updateDate();
    }

    public Date(int year, int month, int day, int hour, int minute) {
        updateCalendar();
        calendar.set(year, month, day, hour, minute);
        updateDate();
    }

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
        month = calendar.get(Calendar.MONTH) + 1; // 0..11 -> 1..12
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
        //as calendar is a horrible mess .compareTo works wrong in our case
        if (date.year > year)
            return -1;
        if (date.year < year)
            return 1;
        if (date.month > month)
            return -1;
        if (date.month < month)
            return 1;
        if (date.day > day)
            return -1;
        if (date.day < day)
            return 1;
        return 0;
    }

    public int howManyWeeksPassed(Date date) {
        Date tmpThis = new Date(this.year, this.month - 1, this.day, 0, 0);
        while (tmpThis.dayOfWeek != 2) {
            tmpThis.previousDay();
        }

        Date tmpDate = new Date(date.year, date.month - 1, date.day, 23, 1);

        long diff = tmpDate.calendar.getTime().getTime() -
                tmpThis.calendar.getTime().getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return (int)(days / 7 + 1);
    }
}
