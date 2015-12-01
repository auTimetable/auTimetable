package ru.spbau.mit.auTimetable;


import android.content.res.AssetManager;
import android.sax.StartElementListener;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by equi on 23.11.15.
 *
 * @author Kravchenko Dima
 */
public class XMLParser {
    private Document doc;

    private int group;
    private int subgroup;

    public XMLParser(AssetManager mgr, int group, int subgroup) {
        this.group = group;
        this.subgroup = subgroup;

        String fileName = group + "_" + subgroup + ".xml";

        //TODO we may need to load this file from server first
        try {
            InputStream inputStream = mgr.open(fileName);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            System.out.println("file found and loaded " + fileName);
        } catch (Exception e) {
            System.out.println("file not found");
            e.printStackTrace();
        }
    }

    public WeekInfo getWeek(int parity) {
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
}
