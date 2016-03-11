package ru.spbau.mit.auTimetable;


import android.app.Activity;
import android.widget.Toast;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
            initializeDoc(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeekInfo getWeek(Date currentDay) {
        try {
            int parity = getParity(currentDay);

            if (parity == -1) {
                return new WeekInfo(group, subgroup, 0);
            } else {
                return getWeekWithParity(parity);
            }
        } catch (Exception e) {
            return new WeekInfo(group, subgroup, 0);
        }
    }

    private int getParity(Date currentDay) {
        try {
            Date firstDay = makeFirstDay();
            if (currentDay.compare(firstDay) < 0) {
                return -1;
            }
            return firstDay.howManyWeeksPassed(currentDay) % 2;
        } catch (Exception e) {
            //parity is set to 0 if no first day specified
        }
        return 0;
    }

    private Date makeFirstDay() {
        Node table = doc.getElementsByTagName("timetable").item(0);
        String stringFirstDay = ((Element)table).getAttribute("first_day");

        String dayParts[] = stringFirstDay.split("\\.");

        int firstDayDay = Integer.parseInt(dayParts[0]);
        int firstDayMonth = Integer.parseInt(dayParts[1]);
        int firstDayYear = Integer.parseInt(dayParts[2]);

        return new Date(firstDayYear, firstDayMonth - 1, firstDayDay);
    }

    private WeekInfo getWeekWithParity(int parity) {
        NodeList weeks = doc.getElementsByTagName("week");

        for (int i = 0; i < weeks.getLength(); i++) {
            Node curWeek = weeks.item(i);
            Element weekElement = (Element) curWeek;
            if (weekElement.getAttribute("parity").equals(Integer.toString(parity))) {
                return parseWeek(weekElement, parity);
            }
        }

        return new WeekInfo(group, subgroup, 0);
    }

    private void initializeDoc(File file) throws IOException,
            ParserConfigurationException, SAXException {
        FileInputStream fis = new FileInputStream(file);
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fis);
        doc.getDocumentElement().normalize();
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
        TimeInterval ti = makeTimeInterval(lesson);
        Map<String, String> infoFieldsValues = new Hashtable<>();

        for (int i = 0; i < infoFields.getLength(); i++) {
            Node curField = infoFields.item(i);
            infoFieldsValues.put(curField.getNodeName(), curField.getTextContent());
        }

        return makeClassInfo(infoFieldsValues, ti);
    }

    private TimeInterval makeTimeInterval(Element lesson) {
        String start = lesson.getAttribute("start");
        String end   = lesson.getAttribute("end");
        return parseTimeInterval(start, end);
    }

    private ClassInfo makeClassInfo(Map<String, String> infoFieldsValues, TimeInterval ti) {
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
            File file = getFile(activity, group, subgroup);
            return handleFile(file, activity, group, subgroup);
        }

        private static File getFile(Activity activity, int group, int subgroup) {
            String fileName = TimetableStringProvider.provideFilePath(group, subgroup);
            return new File(activity.getCacheDir(), fileName);
        }

        private static XMLTimetableParser handleFile(File file, Activity activity,
                                                     int group, int subgroup) {
            if (file.exists()) {
                return new XMLTimetableParser(file, group, subgroup);
            } else {
                return downloadAndWrite(file, activity, group, subgroup);
            }
        }

        private static XMLTimetableParser downloadAndWrite(File file, Activity activity,
                                                            int group, int subgroup) {
            Downloader downloader = new Downloader("timetable", group, subgroup, activity);
            Downloader.ResultContainer timetable = downloader.download();

            if (!timetable.isError) {
                return writeToFile(file, activity, group, subgroup, timetable);
            } else {
                showError(timetable.error, activity);
                return new XMLTimetableParser();
            }
        }

        private static XMLTimetableParser writeToFile(File file, Activity activity,
                                                      int group, int subgroup,
                                                      Downloader.ResultContainer timetable) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                try { // API >= 19 required for try-with-resources
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
