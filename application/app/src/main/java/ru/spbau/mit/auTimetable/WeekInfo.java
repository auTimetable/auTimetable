package ru.spbau.mit.auTimetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by equi on 30.11.15.
 *
 * @author Kravchenko Dima
 */
public class WeekInfo {
    public GlobalGroupId globalGroupId;
    public int parity;

    private List<DayInfo> dayList;
    private List<String> dayNames;

    public WeekInfo(GlobalGroupId globalGroupId, int parity) {
        this.globalGroupId = globalGroupId;
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
        dayNames = Arrays.asList(
                "sunday",
                "monday",
                "tuesday",
                "wednesday",
                "thursday",
                "friday",
                "saturday"
        );
    }

    private void setUpDayList() {
        dayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dayList.add(new DayInfo(dayNames.get(i)));
        }
    }
}
