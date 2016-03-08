package ru.spbau.auTimetable.server;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DaySkeleton {
    public int day;
    public List<ClassSkeleton> classes = new ArrayList<>();

    private int limitFrom, limitTo;
    private HttpServletRequest req;
    private String parityPrefix;

    public DaySkeleton(String parityPrefix, int day, int prevLimit, int curLimit, HttpServletRequest req) {
        this.parityPrefix = parityPrefix;
        this.day = day;
        this.limitFrom = prevLimit + 1; // including
        this.limitTo = curLimit; // excluding
        this.req = req;

        build();
    }

    public String toXML() {
        String res = "";

        res += openTag();
        for (ClassSkeleton classSkeleton : classes) {
            res += classSkeleton.toXML();
        }
        res += closeTag();

        return res;
    }

    private String openTag() {
        return "<day name=\"" + GlobalNamespace.daysEn[day] + "\">";
    }

    private String closeTag() {
        return "</day>";
    }

    private void build() {
        for (int i = limitFrom; i < limitTo; i++) {
            classes.add(new ClassSkeleton(parityPrefix, i, req));
        }
    }
}
