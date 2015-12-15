package ru.spbau.auTimetable.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.List;

public class TimetableServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String groupNumber = req.getParameter("group_number");
        String subgroupNumber = req.getParameter("subgroup_number");
        String key = "default";

        if (groupNumber != null && subgroupNumber != null) {
            key = groupNumber + "_" + subgroupNumber;
        }

        List<UploadedFile> files = ObjectifyService.ofy()
                .load()
                .type(UploadedFile.class)
                .filter("key", key)
                .list();

        if (files.isEmpty()) {
            res.getWriter().print("There is no timetable for this group and subgroup");
        } else {
            BlobKey blobKey = new BlobKey(files.get(0).gsFileName);
            blobstoreService.serve(blobKey, res);
        }
    }
}