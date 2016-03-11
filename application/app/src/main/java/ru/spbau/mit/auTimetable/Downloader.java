package ru.spbau.mit.auTimetable;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
    private int group;
    private int subgroup;
    private Activity activity;
    private ResultContainer result;
    private String prefix;
    private InputStream is;

    private static final int TIME_TO_DOWNLOAD_MS = 5000000; //5 seconds

    public Downloader(String prefix, int group, int subgroup, Activity activity) {
        this.prefix = prefix;
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
            if (prefix.equals("timetable")) {
                downloadUrl(TimetableStringProvider.provideUrl(group, subgroup), result);
            } else {
                downloadUrl(ScoresStringProvider.provideUrl(group, subgroup), result);
            }

            return result;
        } else {
            return new ResultContainer("", "No network connection.", true);
        }
    }

    private void downloadUrl(String myUrl, ResultContainer result) {
        is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = connectToUrl(url);
            handleResponse(conn);
        } catch (IOException e) {
            result.setError("Could not download file.");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    result.setError("Could not close connection.");
                }
            }
        }
    }

    private HttpURLConnection connectToUrl(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIME_TO_DOWNLOAD_MS);//5 seconds to download
        conn.setConnectTimeout(TIME_TO_DOWNLOAD_MS);//5 seconds to connect
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        return conn;
    }

    private void handleResponse(HttpURLConnection conn)
            throws IOException {
        int response = conn.getResponseCode();

        if (response != 200) {
            result.setError("Could not connect to server.");
        } else {
            handleResponse200(conn);
        }
    }

    private void handleResponse200(HttpURLConnection conn) throws IOException {
        is = conn.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");

        String content = s.hasNext() ? s.next() : "";
        if (content.equals("Not found.")) {
            result.setError(content);
        } else {
            result.setContent(content);
        }
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

        public void setError(String error) {
            this.isError = true;
            this.error = error;
        }

        public void setContent(String content) {
            this.isError = false;
            this.content = content;
        }
    }
}
