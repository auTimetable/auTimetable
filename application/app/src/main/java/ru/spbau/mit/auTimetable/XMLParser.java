package ru.spbau.mit.auTimetable;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;


public class XMLParser {
    private Document doc = null;

    private int group;
    private int subgroup;

    private XMLParser() {
        //this one only used if some errors with creating a file occurred.
    }

    private XMLParser(File file, int group, int subgroup) {
        this.group = group;
        this.subgroup = subgroup;

        //TODO we may need to load this file from server first
        try {
            FileInputStream fis = new FileInputStream(file);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fis);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeekInfo getWeek(int parity) {
        if (doc == null) {
            return new WeekInfo(group, subgroup, parity);
        }

        NodeList weeks = doc.getElementsByTagName("week");

        for (int i = 0; i < weeks.getLength(); i++) {
            Node curWeek = weeks.item(i);
            Element weekElement = (Element) curWeek;
            if (weekElement.getAttribute("parity").equals(Integer.toString(parity))) {
                return parseWeek(weekElement, parity);
            }
        }

        return null;
    }

    private WeekInfo parseWeek(Element week, int parity) {
        WeekInfo weekInfo = new WeekInfo(group, subgroup, parity);
        NodeList days = week.getElementsByTagName("day");

        for (int i = 0; i < days.getLength(); i++) {
            Node curDay = days.item(i);
            Element curDayElement = (Element) curDay;
            DayInfo dayInfo = parseDay(curDayElement);
            weekInfo.setDay(dayInfo);
        }

        return weekInfo;
    }

    private DayInfo parseDay(Element day) {
        NodeList classes = day.getElementsByTagName("class");
        DayInfo dayInfo = new DayInfo(day.getAttribute("name"));

        for (int i = 0; i < classes.getLength(); i++) {
            Node curClass = classes.item(i);
            Element curClassElement = (Element) curClass;
            ClassInfo classInfo = parseClass(curClassElement);
            dayInfo.add(classInfo);
        }

        return dayInfo;
    }

    private ClassInfo parseClass(Element lesson) {
        NodeList infoFields = lesson.getElementsByTagName("*");

        String start = lesson.getAttribute("start");
        String end   = lesson.getAttribute("end");
        TimeInterval ti = parseTimeInterval(start, end);

        Map<String, String> infoFieldsValues = new Hashtable<String, String>();

        for (int i = 0; i < infoFields.getLength(); i++) {
            Node curField = infoFields.item(i);
            infoFieldsValues.put(curField.getNodeName(), curField.getTextContent());
        }

        String subject   = infoFieldsValues.get("subject");
        String type      = infoFieldsValues.get("type");
        String classroom = infoFieldsValues.get("classroom");
        String teacher   = infoFieldsValues.get("teacher");

        return new ClassInfo(
                ti,
                subject,
                type,
                classroom,
                teacher
        );
    }

    private TimeInterval parseTimeInterval(String start, String end) {
        String startTimeParts[] = start.split(":");
        String endTimeParts[] = end.split(":");

        int startH = Integer.parseInt(startTimeParts[0]);
        int startM = Integer.parseInt(startTimeParts[1]);
        int endH   = Integer.parseInt(endTimeParts[0]);
        int endM   = Integer.parseInt(endTimeParts[1]);

        return new TimeInterval(startH, startM, endH, endM);
    }

    public static class Builder {
        public static XMLParser build(Context context, Activity activity, int group, int subgroup) {
            String fileName = Integer.toString(group) + "_" + Integer.toString(subgroup) + ".xml";
            File file = new File(context.getCacheDir(), fileName);

            if (file.exists()) {
                return new XMLParser(file, group, subgroup);
            } else {
                try {
                    file = File.createTempFile(fileName, null, context.getCacheDir());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new XMLParser();//this XMLParse is empty
                }
                Downloader downloader = new Downloader(group, subgroup, file, activity);
                if (downloader.run()) {
                    return new XMLParser(file, group, subgroup);
                } else {
                    //for some reason (network is unavailable or server is down) we could not download file
                    return new XMLParser();
                }
            }
        }
    }
}
