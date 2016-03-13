package ru.spbau.mit.auTimetable;

public interface StringProvider {
    String provideUrl(GlobalGroupId globalGroupId);
    String provideFilePath(GlobalGroupId globalGroupId);
}
