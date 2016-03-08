package ru.spbau.auTimetable.server;

import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class GlobalNamespace {
    public static final String gcsBucket = "autimetable-1151.appspot.com";

    public static final GcsService gcsService =
            GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

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

    public static String nDigit(int value, int n) {
        String res = Integer.toString(value);
        while (res.length() < n) {
            res = "0" + res;
        }

        return res;
    }
}
