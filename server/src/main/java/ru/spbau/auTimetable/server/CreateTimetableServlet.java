package ru.spbau.auTimetable.server;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.tools.cloudstorage.*;
import com.googlecode.objectify.ObjectifyService;

public class CreateTimetableServlet extends HttpServlet {
    private TimetableSkeleton timetableSkeleton;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (!UserChecker.isAdminUser(user)) {
            res.sendRedirect("/");
            return;
        }

        String timetable = getXML(req);

        String blobKey = saveFile(timetable).getKeyString();
        saveToObjectify(blobKey);

        res.sendRedirect("/serve?blob-key=" + blobKey);
    }

    private String getXML(HttpServletRequest req) {
        timetableSkeleton = new TimetableSkeleton(req);

        return timetableSkeleton.toXML();
    }

    private BlobKey saveFile(String timetable) throws IOException {
        String fileName = timetableSkeleton.groupNumber + "_" + timetableSkeleton.subgroupNumber + ".xml";
        GcsFilename gcsFileName = new GcsFilename(GlobalNamespace.gcsBucket, fileName);
        GcsOutputChannel outputChannel =
                GlobalNamespace.gcsService.createOrReplace(gcsFileName, GcsFileOptions.getDefaultInstance());
        outputChannel.write(ByteBuffer.wrap(timetable.getBytes()));
        outputChannel.close();

        return GlobalNamespace.blobstoreService.createGsBlobKey(
                "/gs/" + gcsFileName.getBucketName() + "/" + gcsFileName.getObjectName());
    }

    private void saveToObjectify(String blobKey) {
        UploadedFile uploadedFile = new UploadedFile(
                Integer.toString(timetableSkeleton.groupNumber),
                Integer.toString(timetableSkeleton.subgroupNumber),
                blobKey
        );
        ObjectifyService.ofy().save().entity(uploadedFile).now();
    }
}
