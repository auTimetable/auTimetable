package ru.spbau.mit.auTimetable;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
    private final int group;
    private final int subgroup;
    private final Activity activity;
    private final ResultContainer result;

    public Downloader(int group, int subgroup, Activity activity) {
        this.group = group;
        this.subgroup = subgroup;
        this.activity = activity;
        result = new ResultContainer();
    }

    public ResultContainer download() {
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            downloadUrl(createUrl(), result);

            return result;
        } else {
            return new ResultContainer("", "No network connection.", true);
        }
    }

    private String createUrl() {
        return "https://autimetable-1151.appspot.com/timetable?group_number=" + group +
                "&subgroup_number=" + subgroup;
    }

    public static class ResultContainer {
        public String content;
        public String error;
        public boolean isError;

        ResultContainer() {
            this.content = "";
            this.error = "";
            isError = false;
        }

        ResultContainer(String content, String error, boolean isError) {
            this.content = content;
            this.error = error;
            this.isError = isError;
        }
    }

    private void downloadUrl(String myUrl, ResultContainer result) {
        InputStream is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000000);//5 seconds to download
            conn.setConnectTimeout(5000000);//5 seconds to connect
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            if (response != 200) {
                result.isError = true;
                result.error = "Could not connect to server.";
            } else {
                is = conn.getInputStream();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");

                String content = s.hasNext() ? s.next() : "";
                if (content.equals("There is no timetable for this group and subgroup")) {
                    result.isError = true;
                    result.error = content;
                } else {
                    result.content = content;
                }
            }
        } catch (IOException e) {
            result.isError = true;
            result.error = "Could not download file.";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    result.isError = true;
                    result.error = "Could not close connection.";
                }
            }
        }
    }
}
