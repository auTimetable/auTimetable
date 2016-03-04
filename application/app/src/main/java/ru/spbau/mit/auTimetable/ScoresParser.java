package ru.spbau.mit.auTimetable;

import android.app.Activity;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoresParser {
    private final File file;

    private ScoresParser() {
        file = null;
    }

    private ScoresParser(File file) {
        this.file = file;
    }

    public ArrayList<ScoresLink> getLinks() {
        ArrayList<ScoresLink> result = new ArrayList<>();
        if (file == null) {
            return result;
        }

        try {
            Scanner in = new Scanner(file);
            int n = in.nextInt();
            in.nextLine();

            for (int i = 0; i < n; i++) {
                String subject = in.nextLine();
                String link = in.nextLine();

                result.add(new ScoresLink(subject, link));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return result;
    }

    public static class Builder {
        public static ScoresParser build(Activity activity, int group, int subgroup) {
            String fileName = Integer.toString(group) + "_" +
                    Integer.toString(subgroup) + ".scores.xml";
            File file = new File(activity.getCacheDir(), fileName);

            if (file.exists()) {
                return new ScoresParser(file);
            } else {
                Downloader downloader = new Downloader("get_scores", group, subgroup, activity);
                Downloader.ResultContainer scoresList = downloader.download();

                if (!scoresList.isError) {
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        try {
                            fos.write(scoresList.content.getBytes());
                        } finally {
                            fos.close();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                        showError("Could not create file in cache.", activity);

                        return new ScoresParser();
                    }
                    return new ScoresParser(file);
                } else {
                    showError(scoresList.error, activity);
                    return new ScoresParser();
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
