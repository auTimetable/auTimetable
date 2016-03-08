package ru.spbau.auTimetable.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("deprecation")
public class CreateTimetableServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String timetable = getXML(req);

        FileService fileService = FileServiceFactory.getFileService();
        AppEngineFile file = fileService.createNewBlobFile("text/plain");

        FileWriteChannel writeChannel = fileService.openWriteChannel(file, true);

        writeChannel.write(ByteBuffer.wrap(timetable.getBytes()));
        writeChannel.closeFinally();
        BlobKey blobKey = fileService.getBlobKey(file);

        res.sendRedirect("/serve?blob-key=" + blobKey);
    }

    private String getXML(HttpServletRequest req) {
        TimetableSkeleton timetableSkeleton = new TimetableSkeleton(req);
        return timetableSkeleton.toXML();
    }
}
