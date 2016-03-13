package ru.spbau.mit.auTimetable;

/**
 * Created by equi on 11.03.16.
 *
 * @author Kravchenko Dima
 */
public class ScoresStringProvider implements StringProvider {
    @Override
    public String provideUrl(GlobalGroupId globalGroupId) {
        return "https://autimetable-1151.appspot.com/get_scores" +
                "?group_number=" +
                globalGroupId.group +
                "&subgroup_number=" +
                globalGroupId.subgroup;
    }

    @Override
    public String provideFilePath(GlobalGroupId globalGroupId){
        return Integer.toString(globalGroupId.group) +
                "_" +
                Integer.toString(globalGroupId.subgroup) +
                ".scores.xml";
    }
}
