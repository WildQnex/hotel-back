<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="text" var="bndl"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Hotel Europe</title>

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="css/app-style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>

</head>
<body>

<jsp:include page="header.jsp"/>

<main>
    <div class="container">
        <div class="row 20"></div>

        <div class="row">
            <form class="col s12" name="register" action="booking" method="POST">
                <input name="action" type="hidden" value="register">
                <div class="row">
                    <div class="input-field col s6">
                        <input id="first_name" type="text" name="first_name" class="validate" required
                               pattern="[a-zA-Zа-яА-Я]{2,45}" title="<fmt:message key="user.name" bundle="${bndl}"/>">
                        <label for="first_name"><fmt:message key="user.name" bundle="${bndl}"/></label>
                    </div>
                    <div class="input-field col s6">
                        <input id="last_name" type="text" name="last_name" class="validate" required
                               pattern="[a-zA-Zа-яА-Я]{2,45}" title="<fmt:message key="user.name.last" bundle="${bndl}"/>">
                        <label for="last_name"><fmt:message key="user.name.last" bundle="${bndl}"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s6">
                        <input id="middle_name" type="text" name="middle_name" class="validate"
                               pattern="[a-zA-Zа-яА-Я]{0,45}" title="<fmt:message key="user.name.middle" bundle="${bndl}"/>">
                        <label for="middle_name"><fmt:message key="user.name.middle" bundle="${bndl}"/> *</label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s6">
                        <input id="email" type="email" name="email" class="validate" required maxlength="250"
                               pattern="((\w)([-.](\w))?)+@((\w)([-.](\w))?)+.[a-zA-Zа-яА-Я]{2,4}"
                               title="<fmt:message key="user.email" bundle="${bndl}"/>">
                        <label for="email"><fmt:message key="user.email" bundle="${bndl}"/></label>
                    </div>
                    <div class="input-field col s6">
                        <input id="phone_number" type="text" name="phone_number" class="validate" maxlength="18" required
                               pattern="((\+)?\d+?-?\d+-?\d+)|((\+\d+)?(\(\d{3}\))\d{7})|
                               ((\+\d+)?(\(\d{3}\))(\(\d{3}\))?-?\d)|((\+-?(\d){3,18}))"
                               title="<fmt:message key="user.phone" bundle="${bndl}"/>">
                        <label for="phone_number"><fmt:message key="user.phone" bundle="${bndl}"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s6">
                        <input id="password" type="password" name="password" class="validate" required
                               minlength="6" maxlength="60" title="<fmt:message key="user.password" bundle="${bndl}"/>">
                        <label for="password"><fmt:message key="user.password" bundle="${bndl}"/></label>
                    </div>
                    <div class="input-field col s6">
                        <input id="repeat_password" type="password" name="repeat_password" class="validate" required minlength="6"
                               maxlength="60" title="<fmt:message key="user.password.repeat" bundle="${bndl}"/>">
                        <label for="repeat_password"><fmt:message key="user.password.repeat" bundle="${bndl}"/></label>
                    </div>
                </div>

                <div class="row red-text">
                    * - <fmt:message key="register.note" bundle="${bndl}"/>
                </div>

                <c:if test="${not empty register_error}">
                    <div class="row">
                        <div class="col s8 m6 offset-m3 offset-s2 center red-text">${register_error}</div>
                    </div>
                    <c:remove var="register_error" scope="session" />
                </c:if>

                <div class="row">
                    <button class="col s6 m2 offset-s3 offset-m5 btn waves-effect waves-light center" type="submit">
                        <fmt:message key="register.button" bundle="${bndl}"/>
                    </button>
                </div>
            </form>
        </div>
    </div>
</main>

<jsp:include page="footer.jsp"/>

</body>
<!--  Scripts-->
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
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
        document.getElementById("repeat_password").onchange = validatePassword;
        document.getElementById("password").onchange = validatePassword;
    }
    function validatePassword() {
        var pass2 = document.getElementById("repeat_password").value;
        var pass1 = document.getElementById("password").value;
        if (pass1 === pass2)
            document.getElementById("repeat_password").setCustomValidity('');
        else
            document.getElementById("repeat_password").setCustomValidity("<fmt:message key="password.confirmation" bundle="${bndl}"/>");

    }
</script>

<script>
    $( window ).on( "load", function() {
        if ( $('#login-error').length == 1){
            $('#modal').modal('open');
        }
    });
</script>


</html>
