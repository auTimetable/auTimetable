package ru.spbau.auTimetable.server;

import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ScoresListServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String groupNumber = GlobalNamespace.fromParam(req.getParameter("group_number"), "0");
        String subgroupNumber = GlobalNamespace.fromParam(req.getParameter("subgroup_number"), "0");
        String key = groupNumber + "_" + subgroupNumber;

        List<Scores> scores = ObjectifyService.ofy()
                .load()
                .type(Scores.class)
                .filter("key", key)
                .order("date")
                .list();

        req.setAttribute("scores", scores);
        req.setAttribute("group_number", groupNumber);
        req.setAttribute("subgroup_number", subgroupNumber);
        req.getRequestDispatcher("templates/scores_list.jsp").forward(req, res);
    }
}