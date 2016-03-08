package ru.spbau.auTimetable.server;

import javax.servlet.http.HttpServletRequest;

public class TimetableSkeleton {
    public int groupNumber, subgroupNumber, firstDayDay, firstDayMonth, firstDayYear;
    public WeekSkeleton evenWeek, oddWeek;

    private HttpServletRequest req;

    public TimetableSkeleton(HttpServletRequest req) {
        this.req = req;
        build();
    }

    public String toXML() {
        String res = "";

        res += openTag();
        res += evenWeek.toXML();
        res += oddWeek.toXML();
        res += closeTag();

        return res;
    }

    private String openTag() {
        return "<timetable group=\"" + groupNumber + "\" subgroup=\"" + subgroupNumber + "\" first_day=\"" +
                GlobalNamespace.nDigit(firstDayDay, 2) + "." +
                GlobalNamespace.nDigit(firstDayMonth, 2) + "." +
                firstDayYear + "\">";
    }

    private String closeTag() {
        return "</timetable>";
    }

    private void build() {
        setMainIds();
        setWeeks();
    }

    private void setMainIds() {
//        System.out.println("=========     " + req.getParameter("group_number"));
        groupNumber    = GlobalNamespace.fromParamToInt(req.getParameter("group_number"), 0);
        subgroupNumber = GlobalNamespace.fromParamToInt(req.getParameter("subgroup_number"), 0);
        firstDayDay    = GlobalNamespace.fromParamToInt(req.getParameter("first_day_day"), 1);
        firstDayMonth  = GlobalNamespace.fromParamToInt(req.getParameter("first_day_month"), 9);
        firstDayYear   = GlobalNamespace.fromParamToInt(req.getParameter("first_day_year"), 2016);
    }

    private void setWeeks() {
        evenWeek = new WeekSkeleton(0, req);
        oddWeek = new WeekSkeleton(1, req);
    }
}
