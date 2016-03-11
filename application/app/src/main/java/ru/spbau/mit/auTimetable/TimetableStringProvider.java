package ru.spbau.mit.auTimetable;

public class TimetableStringProvider {
    public static String provideUrl(int group, int subgroup) {
        return "https://autimetable-1151.appspot.com/timetable" + "?group_number=" + group +
                "&subgroup_number=" + subgroup;
    }

    public static String provideFilePath(int group, int subgroup) {
        return Integer.toString(group) + "_" + Integer.toString(subgroup) + ".timetable.xml";
    }
}
