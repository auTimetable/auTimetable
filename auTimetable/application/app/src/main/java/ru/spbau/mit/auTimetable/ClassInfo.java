package ru.spbau.mit.auTimetable;

/**
 * Created by equi on 22.11.15.
 *
 * @author Kravchenko Dima
 */
public class ClassInfo {
    public String time;
    public String className;
    public String classroom;
    public String teacherName;

    public ClassInfo(String time, String className, String classroom, String teacherName) {
        this.time        = time;
        this.className   = className;
        this.classroom   = classroom;
        this.teacherName = teacherName;
    }
}
