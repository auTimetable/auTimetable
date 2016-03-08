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
        String groupNumber = GlobalNamespace.fromParam(req.getParameter("group_number"), "0");
        String subgroupNumber = GlobalNamespace.fromParam(req.getParameter("subgroup_number"), "0");
        String key = groupNumber + "_" + subgroupNumber;

        List<UploadedFile> files = ObjectifyService.ofy()
                .load()
                .type(UploadedFile.class)
                .filter("key", key)
                .order("-date")
                .list();

        if (files.isEmpty()) {
            res.getWriter().print("Not found.");
        } else {
            BlobKey blobKey = new BlobKey(files.get(0).gsFileName);
            blobstoreService.serve(blobKey, res);
            res.setContentType("text/xml");
        }
    }
}
