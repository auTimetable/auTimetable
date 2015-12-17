package ru.spbau.auTimetable.server;

public class GlobalFunctions {
    public static String fromParam(String s, String value) {
        if (s == null || s.length() == 0) {
            return value;
        } else {
            return s;
        }
    }
}
