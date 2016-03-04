package ru.spbau.mit.auTimetable;

import java.util.ArrayList;

/**
 * Created by equi on 30.11.15.
 *
 * @author Kravchenko Dima
 */
public class WeekInfo {
    public int group;
    public int subgroup;
    public int parity;

    private ArrayList<DayInfo> dayList;
    private ArrayList<String> dayNames;

    public WeekInfo(int group, int subgroup, int parity) {
        this.group = group;
        this.subgroup = subgroup;
        this.parity = parity;

        setUpDaysNames();
        setUpDayList();
    }

    public void setDay(DayInfo dayInfo) {
        dayList.set(dayNames.indexOf(dayInfo.dayName), dayInfo);
    }

    public DayInfo getDay(String dayName) {
        return dayList.get(dayNames.indexOf(dayName));
    }

    private void setUpDaysNames() {
        dayNames = new ArrayList<>();
        dayNames.add("sunday");
        dayNames.add("monday");
        dayNames.add("tuesday");
        dayNames.add("wednesday");
        dayNames.add("thursday");
        dayNames.add("friday");
        dayNames.add("saturday");
    }

    private void setUpDayList() {
        dayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dayList.add(new DayInfo(dayNames.get(i)));
        }
    }
}
