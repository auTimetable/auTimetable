package ru.spbau.mit.auTimetable;


import android.app.Activity;
import android.widget.Toast;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;


public class XMLTimetableParser {
    private Document doc = null;

    private int group;
    private int subgroup;

    private XMLTimetableParser() {
    }

    private XMLTimetableParser(File file, int group, int subgroup) {
        this.group = group;
        this.subgroup = subgroup;

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

    public WeekInfo getWeek(Date currentDay) {
        if (doc == null) {
            return new WeekInfo(group, subgroup, 0);
        }

        try {
            Node table = doc.getElementsByTagName("timetable").item(0);
            String stringFirstDay = ((Element)table).getAttribute("first_day");

            int parity = 0;
            try {
                String dayParts[] = stringFirstDay.split("\\.");

                int firstDayDay = Integer.parseInt(dayParts[0]);
                int firstDayMonth = Integer.parseInt(dayParts[1]);
                int firstDayYear = Integer.parseInt(dayParts[2]);

                Date firstDay = new Date(firstDayYear, firstDayMonth - 1, firstDayDay);

                if (currentDay.compare(firstDay) < 0) {
                    return new WeekInfo(group, subgroup, 0);
                }

                parity = firstDay.howManyWeeksPassed(currentDay) % 2;
            } catch (Exception e) {
                //parity is set to 0 if no first day specified
            }

            NodeList weeks = doc.getElementsByTagName("week");

            for (int i = 0; i < weeks.getLength(); i++) {
                Node curWeek = weeks.item(i);
                Element weekElement = (Element) curWeek;
                if (weekElement.getAttribute("parity").equals(Integer.toString(parity))) {
                    return parseWeek(weekElement, parity);
                }
            }
        } catch (Exception e) {
            return new WeekInfo(group, subgroup, 0);
        }

        return new WeekInfo(group, subgroup, 0);
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

        Map<String, String> infoFieldsValues = new Hashtable<>();

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

    private TimeInterval parseTimeInterval(String start, String end) throws NumberFormatException {
        String startTimeParts[] = start.split(":");
        String endTimeParts[] = end.split(":");

        int startH = Integer.parseInt(startTimeParts[0]);
        int startM = Integer.parseInt(startTimeParts[1]);
        int endH   = Integer.parseInt(endTimeParts[0]);
        int endM   = Integer.parseInt(endTimeParts[1]);

        return new TimeInterval(startH, startM, endH, endM);
    }

    public static class Builder {
        public static XMLTimetableParser build(Activity activity, int group, int subgroup) {
            String fileName = Integer.toString(group) + "_" +
                    Integer.toString(subgroup) + ".timetable.xml";
            File file = new File(activity.getCacheDir(), fileName);

            if (file.exists()) {
                return new XMLTimetableParser(file, group, subgroup);
            } else {
                Downloader downloader = new Downloader("timetable", group, subgroup, activity);
                Downloader.ResultContainer timetable = downloader.download();

                if (!timetable.isError) {
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        try {
                            fos.write(timetable.content.getBytes());
                        } finally {
                            fos.close();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                        showError("Could not create file in cache.", activity);

                        return new XMLTimetableParser();
                    }
                    return new XMLTimetableParser(file, group, subgroup);
                } else {
                    showError(timetable.error, activity);
                    return new XMLTimetableParser();
                }
            }
        }
    }

    private static void showError(final String error, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
