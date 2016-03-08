package ru.spbau.auTimetable.server;

import javax.servlet.http.HttpServletRequest;

public class WeekSkeleton {
    public int parity;
    public DaySkeleton daySkeleton[] = new DaySkeleton[GlobalNamespace.days.length];

    private String parityPrefix;
    private HttpServletRequest req;
    private int limits[] = new int[GlobalNamespace.days.length];

    WeekSkeleton(int parity, HttpServletRequest req) {
        this.req = req;
        this.parity = parity;

        parityPrefix = "parity" + Integer.toString(parity) + "_";

        build();
    }

    public String toXML() {
        String res = "";

        res += openTag();
        for (DaySkeleton day : daySkeleton) {
            res += day.toXML();
        }
        res += closeTag();

        return res;
    }

    private String openTag() {
        return "<week parity=\"" + parity + "\">";
    }

    private String closeTag() {
        return "</week>";
    }

    private void build() {
        setLimits();
        for (int i = 0; i < GlobalNamespace.days.length; i++) {
            setDay(i);
        }
    }

    private void setLimits() {
        int i = 0;
        for (String day : GlobalNamespace.days) {
            if (i == 0) {
                limits[i] = GlobalNamespace.fromParamToInt(
                        req.getParameter(parityPrefix + day + "_counter"), 0);
            } else {
                limits[i] = limits[i - 1] + GlobalNamespace.fromParamToInt(
                        req.getParameter(parityPrefix + day + "_counter"), 0);
            }
            i++;
        }
    }

    private void setDay(int day) {
        daySkeleton[day] = new DaySkeleton(
                parityPrefix,
                day,
                day == 0 ? -1 : limits[day - 1] - 1,
                limits[day],
                req
        );
    }
}
