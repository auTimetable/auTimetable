package ru.spbau.mit.auTimetable;

import java.util.ArrayList;

public class ScoresParser {
    String content;

    public ScoresParser(String content) {
        this.content = content;
    }

    public ArrayList<ScoresLink> getLinks() {
        ArrayList<ScoresLink> result = new ArrayList<>();
        if (content == null) {
            return result;
        }

        try {
            makeResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return result;
    }

    private void makeResult(ArrayList<ScoresLink> result) {
        String lines[] = content.split("\\r?\\n");

        for (int i = 1; i < lines.length; i += 2) {
            String subject = lines[i];
            String link = lines[i + 1];

            result.add(new ScoresLink(subject, link));
        }
    }
}
