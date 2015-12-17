package ru.spbau.auTimetable.server;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

@Entity
public class Scores {
    @Id
    public Long id;

    //TODO change names `key` and `fullKey`
    @Index
    public String key;
    @Index
    public String fullKey;
    public String groupNumber;
    public String subgroupNumber;
    public String subject;
    public String link;
    @Index
    public Date date;

    /**
     * Takes all important fields
     **/
    public Scores() {
        date = new Date();
    }

    public Scores(String groupNumber, String subgroupNumber,
                  String subject, String link) {
        this();
        this.groupNumber = groupNumber;
        this.subgroupNumber = subgroupNumber;
        this.subject = subject;
        this.link = link;

        key = groupNumber + "_" + subgroupNumber;
        fullKey = subject + "_" + key;
    }

}
