package ru.spbau.auTimetable.server;

public class GlobalNamespace {
    public static String days[] = new String[]{
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота",
            "Воскресенье"
    };

    public static String daysEn[] = new String[]{
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
    };

    public static String fromParam(String s, String value) {
        if (s == null || s.length() == 0) {
            return value;
        } else {
            return s;
        }
    }

    public static int fromParamToInt(String s, int value) {
        try {
            return Integer.parseInt(fromParam(s, Integer.toString(value)));
        } catch(NumberFormatException e) {
            return value;
        }
    }
}
