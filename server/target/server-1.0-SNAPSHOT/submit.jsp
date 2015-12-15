<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>


<html>
    <head>
        <title>Upload Timetable</title>
        <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    </head>
    <body>
        <%
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user == null || !(user.getNickname().equals("equivalence1")
                    || user.getNickname().equals("rozplokhas"))) {
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
                UploadOptions op = UploadOptions.Builder.withGoogleStorageBucketName("autimetable-1151.appspot.com");
                op.maxUploadSizeBytes(100000);
        %>
            <form action="<%= blobstoreService.createUploadUrl("/upload", op) %>" method="post" enctype="multipart/form-data">
                <p> Enter group number </p>
                <input type="text" name="group_number" value="0">
                <input type="text" name="subgroup_number" value="0">
                <p> Chose file to upload (it must be a timetable in xml format) </p>
                <input type="file" name="timetable">
                <input type="submit" value="Submit">
            </form>
        <%
            }
        %>
    </body>
</html>
