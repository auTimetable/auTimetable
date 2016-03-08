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
    private String parityPrefix;

    public ClassSkeleton(String parityPrefix, int classNumber, HttpServletRequest req) {
        this.parityPrefix = parityPrefix;
        this.req = req;
        this.classNumber = classNumber;

        build();
    }

    public String toXML() {
        String res = "";

        res += openTag();
        res += mainInfo();
        res += closeTag();

        return res;
    }

    private String openTag() {
        return "<class start=\"" + GlobalNamespace.nDigit(startHour, 2) + ":" + GlobalNamespace.nDigit(startMinute, 2) +
                "\" end=\"" + GlobalNamespace.nDigit(endHour, 2) + ":" + GlobalNamespace.nDigit(endMinute, 2) + "\">";
    }

    private String mainInfo() {
        String subjectString   = "<subject>" + subject + "</subject>";
        String typeString      = "<type>" + type + "</type>";
        String classroomString = "<classroom>" + classroom + "</classroom>";
        String teacherString   = "<teacher>" + teacher + "</teacher>";

        return subjectString + typeString + classroomString + teacherString;
    }

    private String closeTag() {
        return "</class>";
    }

    private void build() {
        setTime();
        setMainInfo();
    }

    private void setTime() {
        startHour   = GlobalNamespace.fromParamToInt(
                req.getParameterValues(parityPrefix + "start_hour")[classNumber], 0);
        startMinute = GlobalNamespace.fromParamToInt(
                req.getParameterValues(parityPrefix + "start_minute")[classNumber], 0);
        endHour     = GlobalNamespace.fromParamToInt(
                req.getParameterValues(parityPrefix + "end_hour")[classNumber], 0);
        endMinute   = GlobalNamespace.fromParamToInt(
                req.getParameterValues(parityPrefix + "end_minute")[classNumber], 0);
    }

    private void setMainInfo() {
        String none = "None";
        subject   = GlobalNamespace.fromParam(
                req.getParameterValues(parityPrefix + "subject")[classNumber], none);
        type      = GlobalNamespace.fromParam(
                req.getParameterValues(parityPrefix + "type")[classNumber], none);
        classroom = GlobalNamespace.fromParam(
                req.getParameterValues(parityPrefix + "classroom")[classNumber], none);
        teacher   = GlobalNamespace.fromParam(
                req.getParameterValues(parityPrefix + "teacher")[classNumber], none);
    }
}
