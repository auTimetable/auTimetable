package ru.spbau.mit.auTimetable;

import android.app.Activity;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class FileProvider {
    private StringProvider stringProvider;
    private GlobalGroupId globalGroupId;
    private Activity activity;

    public FileProvider(Activity activity,
                        StringProvider stringProvider, GlobalGroupId globalGroupId) {
        this.activity = activity;
        this.stringProvider = stringProvider;
        this.globalGroupId = globalGroupId;
    }

    public String provideContent() { // returns content of the file;
        File file = getFile();
        return createAndGetContent(file);
    }

    public File provideFile() {
        File file = getFile();
        createAndGetContent(file); // returns content. We ignore it
        return file;
    }

    private File getFile() {
        String fileName = stringProvider.provideFilePath(globalGroupId);
        return new File(activity.getCacheDir(), fileName);
    }

    private String createAndGetContent(File file) {
        if (file.exists()) {
            return getFileContent(file);
        } else {
            return downloadAndWrite(file);
        }
    }

    private String getFileContent(File file) {
        Scanner sc;
        StringBuilder res = new StringBuilder();

        try {
            sc = new Scanner(file);
            while(sc.hasNextLine()){
                String nextLine = sc.nextLine() + "\n";
                res.append(nextLine);
            }
        } catch(FileNotFoundException e) {
            //this can never happen. We check if file exists
        }

        return res.toString();
    }

    private String downloadAndWrite(File file) {
        Downloader downloader = new Downloader(globalGroupId, activity, stringProvider);
        Downloader.ResultContainer resultContainer = downloader.download();

        if (!resultContainer.isError) {
            return writeToFile(file, resultContainer);
        } else {
            showError(resultContainer.error, activity);
            return null;
        }
    }

    private String writeToFile(File file, Downloader.ResultContainer resultContainer) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try { // API >= 19 required for try-with-resources
                fos.write(resultContainer.content.getBytes());
            } finally {
                fos.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
            showError("Could not create file in cache.", activity);

            return null;
        }

        return resultContainer.content;
    }

    private static void showError(final String error, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
