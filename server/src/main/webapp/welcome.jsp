<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
</head>

<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);
%>

    <p>Привет, <b>${fn:escapeXml(user.nickname)}</b>!</p>

    <br/>

    <div class="container-fluid">
        <div class="row">
            <a href="../submit.jsp">Загрузить новое расписание</a>
        </div>
    </div>

    <div class="container-fluid">
        <div class="row">
            <a href="../scores.jsp">Просмотр результатов</a>
        </div>
    </div>

    <br/>

    <div class="container-fluid">
        <div class="row">
            <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">выйти</a>
        </div>
    </div>
<%
    } else {
%>
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">войти</a>
<%
    }
%>

</body>
</html>
