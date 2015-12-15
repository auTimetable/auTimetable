package ru.spbau.auTimetable.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class UploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("timetable");
        if (blobKeys == null || blobKeys.isEmpty()) {
            res.sendRedirect("/");
            return;
        }

        String blobKey = blobKeys.get(0).getKeyString();
        String groupNumber = req.getParameter("group_number");
        String subgroupNumber = req.getParameter("subgroup_number");

        UploadedFile file = new UploadedFile(groupNumber, subgroupNumber, blobKey);

        ObjectifyService.ofy().save().entity(file).now();

        res.sendRedirect("/serve?blob-key=" + blobKey);
    }
}
