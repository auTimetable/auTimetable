package ru.spbau.mit.auTimetable;

public class TimetableStringProvider implements StringProvider {
    @Override
    public String provideUrl(GlobalGroupId globalGroupId) {
        return "https://autimetable-1151.appspot.com/timetable" + "?group_number=" +
                globalGroupId.group +
                "&subgroup_number=" +
                globalGroupId.subgroup;
    }

    @Override
    public String provideFilePath(GlobalGroupId globalGroupId) {
        return Integer.toString(globalGroupId.group) +
                "_" +
                Integer.toString(globalGroupId.subgroup) +
                ".timetable.xml";
    }
}
