<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ct" uri="error" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="text" var="bndl"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Hotel Europe</title>

    <!-- CSS  -->
    <link href="css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="css/app-style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>

</head>

<body>

<jsp:include page="../header.jsp"/>

<main>
    <div id="profile" class="container">
        <div class="row"></div>
        <ct:showError scope="${sessionScope}" key="update_profile_error"/>

        <form class="col s12" name="book" action="booking" method="POST">
            <input type="hidden" name="action" value="update_profile">
            <div class="row">
                <div class="input-field col s4 m4">
                    <input id="first_name" type="text" name="first_name" class="validate" required
                           pattern="[a-zA-Zа-яА-Я]{2,45}" value="${user.firstName}"
                           title="<fmt:message key="user.name" bundle="${bndl}"/>">
                    <label for="first_name"><fmt:message key="user.name" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4 m4">
                    <input id="last_name" type="text" name="last_name" class="validate" required
                           pattern="[a-zA-Zа-яА-Я]{2,45}" value="${user.lastName}"
                           title="<fmt:message key="user.name.last" bundle="${bndl}"/>">
                    <label for="last_name"><fmt:message key="user.name.last" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4 m4">
                    <input id="middle_name" type="text" name="middle_name" class="validate"
                           pattern="[a-zA-Zа-яА-Я]{0,45}" value="${user.middleName}"
                           title="<fmt:message key="user.name.middle" bundle="${bndl}"/>">
                    <label for="middle_name"><fmt:message key="user.name.middle" bundle="${bndl}"/> *</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4 m4">
                    <label for="balance"><fmt:message key="user.balance" bundle="${bndl}"/></label>
                    <input disabled name="balance" id="balance" type="number" value="${user.balance}">
                </div>
                <div class="input-field col s4 m4">
                    <input id="email" type="email" name="email" class="validate" value="${user.email}"
                           disabled title="<fmt:message key="user.email" bundle="${bndl}"/>">
                    <label for="email"><fmt:message key="user.email" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4 m4">
                    <label for="role"><fmt:message key="user.role" bundle="${bndl}"/></label>
                    <input disabled name="role" id="role" type="text" value="${user.role}">
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4 m4">
                    <input id="phone_number" type="text" name="phone_number" class="validate" maxlength="18" required
                           pattern="((\+)?\d+?-?\d+-?\d+)|((\+\d+)?(\(\d{3}\))\d{7})|
                               ((\+\d+)?(\(\d{3}\))(\(\d{3}\))?-?\d)|((\+-?(\d){3,18}))"
                           value="${user.phoneNumber}" title="<fmt:message key="user.phone" bundle="${bndl}"/>">
                    <label for="phone_number"><fmt:message key="user.phone" bundle="${bndl}"/></label>
                </div>
            </div>
            <div class="row">
                <button class="col s6 m2 offset-s3 offset-m5 btn amber accent-4 waves-effect waves-light center"
                        type="submit">
                    <fmt:message key="profile.change" bundle="${bndl}"/>
                </button>
            </div>
        </form>
        <form class="col s12" action="booking" method="POST">
            <input type="hidden" name="action" value="update_password">
            <div class="row"></div>
            <div class="row">
                <div class="input-field col s4">
                    <input id="current_password" type="password" name="current_password" class="validate" required
                           minlength="6"
                           maxlength="60" title="<fmt:message key="user.password.current" bundle="${bndl}"/>">
                    <label for="current_password"><fmt:message key="user.password.current" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4">
                    <input id="new_password" type="password" name="new_password" class="validate" required minlength="6"
                           maxlength="60" title="<fmt:message key="user.password" bundle="${bndl}"/>">
                    <label for="new_password"><fmt:message key="user.password" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4">
                    <input id="repeat_new_password" type="password" name="repeat_new_password" class="validate" required
                           minlength="6"
                           maxlength="60" title="<fmt:message key="user.password.repeat" bundle="${bndl}"/>">
                    <label for="repeat_new_password"><fmt:message key="user.password.repeat" bundle="${bndl}"/></label>
                </div>
            </div>
            <div class="row">
                <button class="col s6 m2 offset-s3 offset-m5 btn amber accent-4 waves-effect waves-light center"
                        type="submit">
                    <fmt:message key="profile.password.change" bundle="${bndl}"/>
                </button>
            </div>
        </form>
    </div>
</main>


<jsp:include page="../footer.jsp"/>

</body>


<!--  Scripts-->
<script src="js/jquery-2.1.1.min.js"></script>
<script src="js/materialize.js"></script>
<script src="js/init.js"></script>

<script>
    $('.modal').modal({
            dismissible: true, // Modal can be dismissed by clicking outside of the modal
            opacity: .5, // Opacity of modal background
            inDuration: 300, // Transition in duration
            outDuration: 200, // Transition out duration
            startingTop: '4%', // Starting top style attribute
            endingTop: '10%', // Ending top style attribute
        }
    );
</script>

<script type="text/javascript" language="JavaScript">
    window.onload = function () {
        document.getElementById("repeat_new_password").onchange = validatePassword;
        document.getElementById("new_password").onchange = validatePassword;
    }

    function validatePassword() {
        var pass2 = document.getElementById("repeat_new_password").value;
        var pass1 = document.getElementById("new_password").value;
        if (pass1 === pass2)
            document.getElementById("repeat_new_password").setCustomValidity('');
        else
            document.getElementById("repeat_new_password").setCustomValidity("<fmt:message key="password.confirmation" bundle="${bndl}"/>");

    }
</script>

<script>
    $(window).on("load", function () {
        if ($('#login-error').length == 1) {
            $('#modal').modal('open');
        }
    });
</script>

<script>
    $('.datepicker').pickadate({
        selectMonths: true, // Creates a dropdown to control month
        selectYears: 15, // Creates a dropdown of 15 years to control year,
        today: 'Today',
        clear: 'Clear',
        close: 'Ok',
        closeOnSelect: false // Close upon selecting a date,
    });
</script>

</html>