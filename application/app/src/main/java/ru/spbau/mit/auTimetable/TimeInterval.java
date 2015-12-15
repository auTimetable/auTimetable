package ru.spbau.mit.auTimetable;

/**
 * Created by equi on 28.11.15.
 *
 * @author Kravchenko Dima
 */
public class TimeInterval {
    public int start_hour;
    public int start_minute;
    public int end_hour;
    public int end_minute;

    public TimeInterval(int sh, int sm, int eh, int em) {
        this.start_hour = sh;
        this.start_minute = sm;
        this.end_hour = eh;
        this.end_minute = em;
    }

    public String toString() {
        return start_hour + ":" + toLen2(start_minute) + "--" +
               end_hour + ":" + toLen2(end_minute);
    }

    public int compare(int h, int m) {
        int start = start_hour * 60 + start_minute;
        int end   = end_hour * 60 + end_minute;
        int point = h * 60 + m;

        if (point < start) {
            return -1;
        }
        if (point >= start && point <= end) {
            return 0;
        }
        return 1;
    }

    private String toLen2(int x) {
        String s = Integer.toString(x);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }
}
