package ru.spbau.auTimetable.server;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

@Entity
public class UploadedFile {
    @Id public Long id;

    @Index String key;
    public String groupNumber;
    public String subgroupNumber;
    public String gsFileName;
    public Date date;

    /**
     * Takes all important fields
     **/
    public UploadedFile() {
        date = new Date();
    }

    public UploadedFile(String groupNumber, String subgroupNumber,
                        String gsFileName) {
        this();
        this.groupNumber = groupNumber;
        this.subgroupNumber = subgroupNumber;
        this.gsFileName = gsFileName;

        key = groupNumber + "_" + subgroupNumber;
    }

}
