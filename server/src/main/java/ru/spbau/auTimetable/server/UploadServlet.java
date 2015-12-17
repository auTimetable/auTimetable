package ru.spbau.auTimetable.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class UploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (!UserChecker.isAdminUser(user)) {
            res.sendRedirect("/");
            return;
        }

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("timetable");
        if (blobKeys == null || blobKeys.isEmpty()) {
            res.sendRedirect("/");
            return;
        }

        String blobKey = blobKeys.get(0).getKeyString();
        String groupNumber = GlobalFunctions.fromParam(req.getParameter("group_number"), "0");
        String subgroupNumber = GlobalFunctions.fromParam(req.getParameter("subgroup_number"), "0");

        UploadedFile file = new UploadedFile(groupNumber, subgroupNumber, blobKey);

        ObjectifyService.ofy().save().entity(file).now();

        res.sendRedirect("/serve?blob-key=" + blobKey);
    }
}
