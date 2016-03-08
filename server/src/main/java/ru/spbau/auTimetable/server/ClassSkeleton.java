package ru.spbau.auTimetable.server;

import javax.servlet.http.HttpServletRequest;

public class ClassSkeleton {
    public int startHour, startMinute, endHour, endMinute;
    public String subject;
    public String type;
    public String classroom;
    public String teacher;

    private HttpServletRequest req;
    private int classNumber;

    public ClassSkeleton(int classNumber, HttpServletRequest req) {
        this.req = req;
        this.classNumber = classNumber;

        build();
    }

    private void build() {
        setTime();
        setMainInfo();
    }

    private void setTime() {
        startHour   = GlobalNamespace.fromParamToInt(req.getParameterValues("start_hour")[classNumber], 0);
        startMinute = GlobalNamespace.fromParamToInt(req.getParameterValues("start_minute")[classNumber], 0);
        endHour     = GlobalNamespace.fromParamToInt(req.getParameterValues("end_hour")[classNumber], 0);
        endMinute   = GlobalNamespace.fromParamToInt(req.getParameterValues("end_minute")[classNumber], 0);
    }

    private void setMainInfo() {
        String none = "None";
        subject   = GlobalNamespace.fromParam(req.getParameterValues("subject")[classNumber], none);
        type      = GlobalNamespace.fromParam(req.getParameterValues("type")[classNumber], none);
        classroom = GlobalNamespace.fromParam(req.getParameterValues("classroom")[classNumber], none);
        teacher   = GlobalNamespace.fromParam(req.getParameterValues("teacher")[classNumber], none);
    }
}
