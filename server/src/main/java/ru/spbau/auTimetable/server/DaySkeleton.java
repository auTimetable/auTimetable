package ru.spbau.auTimetable.server;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DaySkeleton {
    public String day;
    public List<ClassSkeleton> classes = new ArrayList<>();

    private int limitFrom, limitTo;
    private HttpServletRequest req;

    public DaySkeleton(String day, int prevLimit, int curLimit, HttpServletRequest req) {
        this.day = day;
        this.limitFrom = prevLimit + 1; // including
        this.limitTo = curLimit; // excluding
        this.req = req;

        build();
    }

    private void build() {
        for (int i = limitFrom; i < limitTo; i++) {
            classes.add(new ClassSkeleton(i, req));
        }
    }
}
