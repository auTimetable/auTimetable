<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.spbau.auTimetable.server.UserChecker" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%-- for some reason this import does not work and I could not find out why --%>
<%-- so I have to manually create `day` list instead of using Arrays.asList --%>
<%-- <%@ page import="java.util.Arrays" %> --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="js/create.js"></script>
    </head>

    <body>

        <%
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (!UserChecker.isAdminUser(user)) {
            pageContext.setAttribute("user", user);
        %>
            <h1> You are not allowed to create files on the server </h1>
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
        %>
            
            <a href=".."> обратно на главную </a>

            <br />

            <form class="form-horizontal col-md-10 col-md-offset-1" role="form" action="/create_timetable" method="post">
                <div class="form-group center-block">
                    <label for="group_number" class="col-sm-2 control-label">Номер группы</label>
                    <div class="col-sm-2">
                        <input type="number" class="form-control" name="group_number" id="group_number" placeholder="номер группы">
                    </div>
                </div>

                <div class="form-group">
                    <label for="subgroup_number" class="col-sm-2 control-label">Номер подгруппы</label>
                    <div class="col-sm-2">
                        <input type="number" class="form-control" name="subgroup_number" id="subgroup_number" placeholder="номер подгруппы">
                    </div>
                </div>

                <div class="form-group">
                    <label for="first_day_day" class="col-sm-2 control-label">Первый день учебы</label>
                    <div class="col-sm-1">
                        <input type="number" class="form-control" name="first_day_day" id="first_day_day" placeholder="День">
                    </div>
                    <div class="col-sm-1">
                        <input type="number" class="form-control" name="first_day_month" id="first_day_month" placeholder="Месяц">
                    </div>
                    <div class="col-sm-2">
                        <input type="number" class="form-control" name="first_day_year" id="first_day_year" placeholder="Год">
                    </div>
                    <span class="label label-info">Info</span>
                    <small>Первая неделя считается нечетной. Т.е. введенное Вами число -- первый день первой нечетной недели</small>
                </div>

                <br/>
                <%
                /** doesn't work for some reason which I could not find out
                 *List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг",
                 *       "Пятница", "Суббота", "Воскресенье");
                 */
                List<String> days = new ArrayList<String>();

                days.add("Понедельник");
                days.add("Вторник");
                days.add("Среда");
                days.add("Четверг");
                days.add("Пятница");
                days.add("Суббота");
                days.add("Воскресенье");
                %>

                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#parity0_week">Четная неделя</a></li>
                    <li><a data-toggle="tab" href="#parity1_week">Нечетная неделя</a></li>
                </ul>

                <div class="tab-content">
                    <div id="parity0_week" class="tab-pane fade in active">
                        <%--<div class="panel-group">--%>
                            <%
                            for (String day : days) {
                            %>
                                <div class="form-group panel panel-success center-block">

                                    <div class="panel-heading">
                                        <%= day %>
                                    </div>

                                    <div class="panel-body">
                                        <div class="panel-group" id="parity0_<%= day %>_classes">
        <%-- New classes will be added here --%>
                                        </div>
                                    </div>

                                    <div class="panel-footer">
                                        <button type="button" class="btn btn-success center-block" onclick="addClass('parity0', '<%= day %>')">
                                            Добавить пару
                                        </button>
                                    </div>

                                </div>
                                <input type="number" name="parity0_<%= day %>_counter" id="parity0_<%= day %>_counter" value="0" style="display: none;">
                            <%
                            }
                            %>
                        <%--</div>--%>
                    </div>
                    <div id="parity1_week" class="tab-pane fade">
                        <%--<div class="panel-group">--%>
                            <%
                            for (String day : days) {
                            %>
                                <div class="form-group panel panel-success center-block">

                                    <div class="panel-heading">
                                        <%= day %>
                                    </div>

                                    <div class="panel-body">
                                        <div class="panel-group" id="parity1_<%= day %>_classes">
        <%-- New classes will be added here --%>
                                        </div>
                                    </div>

                                    <div class="panel-footer">
                                        <button type="button" class="btn btn-success center-block" onclick="addClass('parity1', '<%= day %>')">
                                            Добавить пару
                                        </button>
                                    </div>

                                </div>
                                <input type="number" name="parity1_<%= day %>_counter" id="parity1_<%= day %>_counter" value="0" style="display: none;">
                            <%
                            }
                            %>
                        <%--</div>--%>
                    </div>
                </div>

                <button type="submit" class="btn btn-success center-block">Создать</button>
            </form>

<%-- beautifull hack. see https://stackoverflow.com/questions/18673860/defining-a-html-template-to-append-using-jquery --%>
<%-- <script id="hidden-template" type="text/x-custom-template"> --%>
<div id="hidden-template" style="display: none;">
    <div class="panel panel-default center-block" id="class_form">
        <div class="panel-content">
            <hr>
            <div class="row">
                <label for="start_hour" id="start_hour_label" class="col-sm-2 control-label">Время начала</label>
                <div class="col-sm-2">
                    <input type="number" class="form-control" name="start_hour" id="start_hour" placeholder="Часы">
                </div>
                <div class="col-sm-2">
                    <input type="number" class="form-control" name="start_minute" id="start_minute" placeholder="Минуты">
                </div>
            </div>

            <hr>

            <div class="row">
                <label for="end_hour" id="end_hour_label" class="col-sm-2 control-label">Время окончания</label>
                <div class="col-sm-2">
                    <input type="number" class="form-control" name="end_hour" id="end_hour" placeholder="Часы">
                </div>
                <div class="col-sm-2">
                    <input type="number" class="form-control" name="end_minute" id="end_minute" placeholder="Минуты">
                </div>
            </div>

            <hr>

            <div class="row">
                <label for="subject" id="subject_label" class="col-sm-2 control-label">Наименование предмета</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="subject" id="subject" placeholder="Предмет">
                </div>
            </div>

            <hr>

            <div class="row">
                <label for="type" id="type_label" class="col-sm-2 control-label">Тип пары</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="type" id="type" placeholder="Лекция/Практика/etc">
                </div>
            </div>

            <hr>

            <div class="row">
                <label for="classroom" id="classroom_label" class="col-sm-2 control-label">Номер кабинета</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" name="classroom" id="classroom" placeholder="Номер кабинета">
                </div>
                <span class="label label-info">Info</span>
                <small>Номер кабинета не обязан быть числом. Например, "422 (ФТШ)" -- корректный "номер"</small>
            </div>

            <hr>

            <div class="row">
                <label for="teacher" id="teacher_label" class="col-sm-2 control-label">ФИО преподавателя</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="teacher" id="teacher" placeholder="ФИО преподавателя">
                </div>
            </div>

            <hr>
        </div>

        <div class="panel-footer">
            <button type="button" id="delete_button" class="btn btn-danger center-block">
                Удалить пару
            </button>
        </div>
    </div>
</div>
<%-- </script> --%>

        <%
        }
        %>
    </body>
</html>
