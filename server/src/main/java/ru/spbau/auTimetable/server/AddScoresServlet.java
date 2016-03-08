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

public class AddScoresServlet extends HttpServlet {
/*
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (!UserChecker.isAdminUser(user)) {
            res.sendRedirect("/");
            return;
        }

        String groupNumber = (String)req.getAttribute("group_number");
        String subgroupNumber = (String)req.getAttribute("subgroup_number");

        String subject = (String)req.getAttribute("subject");
        String link = (String)req.getAttribute("link");

        Scores scores = new Scores(groupNumber, subgroupNumber, subject, link);

        ObjectifyService.ofy().save().entity(scores).now();

        res.sendRedirect("/scores_list?group_number=" + groupNumber +
                "&subgroup_number=" + subgroupNumber);
    }
*/ //post doesn't work. still no ideas and nobody on stackoverflow knows (but leave likes)
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (!UserChecker.isAdminUser(user)) {
            res.sendRedirect("/");
            return;
        }

        String groupNumber = GlobalNamespace.fromParam(req.getParameter("group_number"), "0");
        String subgroupNumber = GlobalNamespace.fromParam(req.getParameter("subgroup_number"), "0");

        String subject = req.getParameter("subject");
        String link = req.getParameter("link");

        Scores scores = new Scores(groupNumber, subgroupNumber, subject, link);

        ObjectifyService.ofy().save().entity(scores).now();

        res.sendRedirect("/scores_list?group_number=" + groupNumber +
                "&subgroup_number=" + subgroupNumber);
    }
}