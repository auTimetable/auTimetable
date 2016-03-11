package ru.spbau.mit.auTimetable;

/**
 * Created by equi on 11.03.16.
 *
 * @author Kravchenko Dima
 */
public class ScoresStringProvider {
    public static String provideUrl(int group, int subgroup) {
        return "https://autimetable-1151.appspot.com/get_scores" + "?group_number=" + group +
                "&subgroup_number=" + subgroup;
    }

    public static String provideFilePath(int group, int subgroup){
        return Integer.toString(group) + "_" + Integer.toString(subgroup) + ".scores.xml";
    }
}
