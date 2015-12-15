package ru.spbau.mit.auTimetable;

/**
 * Created by equi on 22.11.15.
 *
 * @author Kravchenko Dima
 */
public class ClassInfo {
    public TimeInterval time;
    public String subject;
    public String classType;
    public String classroom;
    public String teacherName;

    public ClassInfo(TimeInterval time, String subject, String classType,
                     String classroom, String teacherName) {
        this.time        = time;
        this.subject   = subject;
        this.classType   = classType;
        this.classroom   = classroom;
        this.teacherName = teacherName;
    }
}
