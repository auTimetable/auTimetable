<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions" %>
<%@ page import="ru.spbau.auTimetable.server.UserChecker" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="ru.spbau.auTimetable.server.GlobalNamespace" %>
<%@ page import="ru.spbau.auTimetable.server.UploadedFile" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<html>
    <head>
        <title>Upload Timetable</title>
        <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
    </head>

    <body>
        <%
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (!UserChecker.isAdminUser(user)) {
            pageContext.setAttribute("user", user);
        %>
            <h1> You are not allowed to upload file on the server </h1>
            <%
            if (user != null) {
                pageContext.setAttribute("user", user);
            %>
                <p>your current user is <b>${fn:escapeXml(user.nickname)}</b></p>
                <a href="../">back to main page.</a>
            <%
            } else {
            %>
                <p> please, login on <a href="../">main</a> page.</p>
            <%
            }
            %>
        <%
        } else {
            List<UploadedFile> files = ObjectifyService.ofy()
                    .load()
                    .type(UploadedFile.class)
                    .order("-date")
                    .list();

            Map<String, List<UploadedFile>> map = new HashMap<String, List<UploadedFile>>();
            for (UploadedFile uploadedFile : files) {
                if (!map.containsKey(uploadedFile.key))
                    map.put(uploadedFile.key, new ArrayList<UploadedFile>());
                map.get(uploadedFile.key).add(uploadedFile);
            }

            Set<String> keys = map.keySet();
        %>
            <a href="../">Обратно на главную</a>
        <%
            for (String key : keys) {
        %>
                <h1 align="center">Файлы для группы <%= map.get(key).get(0).groupNumber %> подгруппы <%= map.get(key).get(0).subgroupNumber %> </h1>
                <%
                Boolean first = true;
                for (UploadedFile file : map.get(key)) {
                    String blobKey = file.gsFileName;
                    if (first) {
                %>
                        <p align="center"><font color="red">Текущее: </font><a href="../serve?blob-key=<%= blobKey %>"><%= blobKey %></a></p>
                <%
                    } else {
                %>
                        <p align="center"><a href="../serve?blob-key=<%= blobKey %>"><%= blobKey %></a></p>
                <%
                    }
                    first = false;
                }
                %>
                <hr>
        <%
            }
        }
        %>
    </body>
</html>
