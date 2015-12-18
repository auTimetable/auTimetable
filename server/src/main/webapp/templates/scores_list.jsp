<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.spbau.auTimetable.server.Scores" %>
<%@ page import="ru.spbau.auTimetable.server.UserChecker" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <title>Upload Timetable</title>
        <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="js/scores_list.js"></script>
    </head>
    <body>
        <%
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();

            List<Scores> scoresList = (List<Scores>)request.getAttribute("scores");

            String groupNumber = (String)request.getAttribute("group_number");
            String subgroupNumber = (String)request.getAttribute("subgroup_number");

            if (scoresList != null && scoresList.size() > 0) {
        %>
        <h2> Список таблиц с результатами </h2>

        <table class="table table-hover" id="scores_table">
            <tr>
                <th>#</th>
                <th>Subject</th>
                <th>Link</th>
            <%
                if (UserChecker.isAdminUser(user)) {
            %>
                    <th>Delete?</th>
            <%
                } else {
            %>
                    <th></th>
            <%
                }
            %>
            </tr>
        <%
                int index = 0;
                for (Scores scores : scoresList) {
                    index++;
        %>
            <tr>
                <td><%= index %></td>
                <td><%= scores.subject %></td>
                <td><a href="<%= scores.link %>"><%= scores.link %></a></td>
                <%
                    if (UserChecker.isAdminUser(user)) {
                %>
                        <td>
                            <button class="delete_button" onclick="deleteScores('<%= scores.fullKey %>', <%= groupNumber %>, <%= subgroupNumber %>)">
                                <span class="glyphicon glyphicon-remove" style="color:red"></span>
                            </button>
                        </td>
                <%
                    } else {
                %>
                        <td></td>
                <%
                    }
                %>
            </tr>
        <%
                }
        %>
        </table>
        <%
            } else {
        %>
        <h1> Никаких записей не найдено </h1>
        <%
            }
        %>

        <%
            if (UserChecker.isAdminUser(user)) {
        %>

            <br/>

            <h2> Вы можете добавить ссылки на результаты </h2>
            <form action="/add_scores" method="get" enctype="multipart/form-data" id="add_link_form">
              <input type="hidden" name="group_number" value="<%= groupNumber %>">
              <input type="hidden" name="subgroup_number" value="<%= subgroupNumber %>">
              <div class="form-group">
                <label for="subject" class="col-sm-2 control-label">Предмет</label>
                <div class="col-sm-10">
                  <input type="text" class="form-control" name="subject" id="subject" placeholder="предмет">
                </div>
              </div>
              <div class="form-group">
                <label for="link" class="col-sm-2 control-label">Ссылка</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="link" id="link" placeholder="ссылка">
                </div>
              </div>
              <button type="submit" class="btn btn-success">Добавить</button>
            </form>

        <%
            }
        %>
            <br/>

            <div class="container-fluid">
                <div class="row">
                    <a href="../scores.jsp"> Назад </a>
                </div>
            </div>

            <div class="container-fluid">
                <div class="row">
                    <a href="../welcome.jsp"> Обратно на главную </a>
                </div>
            </div>

    </body>
</html>
