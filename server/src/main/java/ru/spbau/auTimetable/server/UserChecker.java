package ru.spbau.auTimetable.server;

import com.google.appengine.api.users.User;

import java.util.Arrays;
import java.util.List;

public class UserChecker {
    private static List<String> adminUsers =
            Arrays.asList("equivalence1", "rozplokhas");

    public static boolean isAdminUser(User user) {
        return (user != null && adminUsers.contains(user.getNickname()));
    }
}
