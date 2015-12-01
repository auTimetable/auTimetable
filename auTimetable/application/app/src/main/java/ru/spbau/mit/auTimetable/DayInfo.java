package ru.spbau.mit.auTimetable;

import java.util.ArrayList;

/**
 * Created by equi on 30.11.15.
 *
 * @author Kravchenko Dima
 */
public class DayInfo {
    public String dayName;
    public ArrayList<ClassInfo> classList;

    public DayInfo(String dayName) {
        this.dayName = dayName;
        classList = new ArrayList<ClassInfo>();
    }

    public void add(ClassInfo classInfo) {
        classList.add(classInfo);
    }
}
