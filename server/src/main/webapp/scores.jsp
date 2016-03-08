<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions" %>
<%@ page import="ru.spbau.auTimetable.server.UserChecker" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <title>Upload Timetable</title>
        <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
    </head>

    <body>
        <h1> Посмотреть список результатов </h1>

        <form action="/scores_list" method="get" enctype="multipart/form-data">
            <div class="form-group">
                <label for="group_number" class="col-sm-2 control-label">Номер группы</label>
                <div class="col-sm-10">
                    <input type="number" class="form-control" name="group_number" id="group_number" placeholder="номер группы">
                </div>
            </div>

            <div class="form-group">
                <label for="subgroup_number" class="col-sm-2 control-label">Номер подгруппы</label>
                <div class="col-sm-10">
                    <input type="number" class="form-control" name="subgroup_number" id="subgroup_number" placeholder="номер подгруппы">
                </div>
            </div>

            <button type="submit" class="btn btn-success">Получить</button>
        </form>

        <br/>

        <div class="container-fluid">
            <div class="row">
                <a href="../welcome.jsp"> Обратно на главную </a>
            </div>
        </div>
    </body>
</html>
