package ru.spbau.auTimetable.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetScoresServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String groupNumber = GlobalNamespace.fromParam(req.getParameter("group_number"), "0");
        String subgroupNumber = GlobalNamespace.fromParam(req.getParameter("subgroup_number"), "0");
        String key = groupNumber + "_" + subgroupNumber;

        List<Scores> scoresList = ObjectifyService.ofy()
                .load()
                .type(Scores.class)
                .filter("key", key)
                .order("-date")
                .list();

        res.setContentType ("text/plain;charset=utf-8");
        PrintWriter output = res.getWriter();

        output.println(scoresList.size());
        for (Scores scores : scoresList) {
            output.println(scores.subject);
            output.println(scores.link);
        }
        output.close();
    }
}
