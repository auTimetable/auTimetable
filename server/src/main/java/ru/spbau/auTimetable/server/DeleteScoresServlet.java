package ru.spbau.auTimetable.server;

import com.googlecode.objectify.ObjectifyService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DeleteScoresServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (!UserChecker.isAdminUser(user)) {
            res.sendRedirect("/");
            return;
        }

        String groupNumber = GlobalFunctions.fromParam(req.getParameter("group_number"), "0");
        String subgroupNumber = GlobalFunctions.fromParam(req.getParameter("subgroup_number"), "0");

        String scoresFullKey = req.getParameter("scores_full_key");

        List<Scores> scores = ObjectifyService.ofy()
                .load()
                .type(Scores.class)
                .filter("fullKey", scoresFullKey)
                .list();

        if (scores != null && scores.size() > 0) {
            ObjectifyService.ofy().delete().entity(scores.get(0)).now();
        }

        res.sendRedirect("/scores_list?group_number=" + groupNumber +
                "&subgroup_number=" + subgroupNumber);
    }
}