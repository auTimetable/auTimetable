package ru.spbau.mit.auTimetable;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Downloader {
    private int group;
    private int subgroup;
    private File file;
    private Activity activity;

    public Downloader(int group, int subgroup, File file, Activity activity) {
        this.group = group;
        this.subgroup = subgroup;
        this.file = file;
    }

    public boolean run() {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            //this exception can not occur here. I checked it in XMLParser
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

        } else {
            return false;
        }
    }
}
