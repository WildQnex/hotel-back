<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ct" uri="http://martyniuk.by" %>
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
        <ct:showMessage color="red" key="updateProfileError"/>

        <form class="col s12" name="book" action="booking" method="POST">
            <input type="hidden" name="action" value="admin_update_user_profile">
            <input type="hidden" name="id" value="${userProfile.id}">
            <div class="row">
                <div class="input-field col s4 m4">
                    <input id="firstName" type="text" name="firstName" class="validate" required
                           pattern="[a-zA-Zа-яА-Я]{2,45}" value="${userProfile.firstName}"
                           title="<fmt:message key="user.name" bundle="${bndl}"/>">
                    <label for="firstName"><fmt:message key="user.name" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4 m4">
                    <input id="lastName" type="text" name="lastName" class="validate" required
                           pattern="[a-zA-Zа-яА-Я]{2,45}" value="${userProfile.lastName}"
                           title="<fmt:message key="user.name.last" bundle="${bndl}"/>">
                    <label for="lastName"><fmt:message key="user.name.last" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4 m4">
                    <input id="middleName" type="text" name="middleName" class="validate"
                           pattern="[a-zA-Zа-яА-Я]{0,45}" value="${userProfile.middleName}"
                           title="<fmt:message key="user.name.middle" bundle="${bndl}"/>">
                    <label for="middleName"><fmt:message key="user.name.middle" bundle="${bndl}"/> *</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4 m4">
                    <label for="balance"><fmt:message key="user.balance" bundle="${bndl}"/></label>
                    <input disabled name="balance" id="balance" type="number" value="${userProfile.balance}">
                </div>
                <div class="input-field col s4 m4">
                    <input id="email" type="email" name="email" class="validate" value="${userProfile.email}"
                           disabled title="<fmt:message key="user.email" bundle="${bndl}"/>">
                    <label for="email"><fmt:message key="user.email" bundle="${bndl}"/></label>
                </div>
                <div class="input-field col s4 m4">
                    <select id="role" name="role">
                        <option value="" disabled><fmt:message key="user.role" bundle="${bndl}"/></option>
                        <c:choose>
                            <c:when test="${userProfile.role.toString().equalsIgnoreCase('Admin')}">
                                <option value="user"><fmt:message key="user.user" bundle="${bndl}"/></option>
                                <option selected value="admin"><fmt:message key="user.admin" bundle="${bndl}"/></option>
                            </c:when>
                            <c:otherwise>
                                <option selected value="user"><fmt:message key="user.user" bundle="${bndl}"/></option>
                                <option value="admin"><fmt:message key="user.admin" bundle="${bndl}"/></option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                    <label for="role"><fmt:message key="user.role" bundle="${bndl}"/></label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4 m4">
                    <input id="phoneNumber" type="text" name="phoneNumber" class="validate" maxlength="18" required
                           pattern="((\+)?\d+?-?\d+-?\d+)|((\+\d+)?(\(\d{3}\))\d{7})|
                               ((\+\d+)?(\(\d{3}\))(\(\d{3}\))?-?\d)|((\+-?(\d){3,18}))"
                           value="${userProfile.phoneNumber}" title="<fmt:message key="user.phone" bundle="${bndl}"/>">
                    <label for="phoneNumber"><fmt:message key="user.phone" bundle="${bndl}"/></label>
                </div>
            </div>
            <div class="row">
                <button class="col s6 m2 offset-s3 offset-m5 btn amber accent-4 waves-effect waves-light center"
                        type="submit">
                    <fmt:message key="profile.change" bundle="${bndl}"/>
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
        document.getElementById("repeatNewPassword").onchange = validatePassword;
        document.getElementById("newPassword").onchange = validatePassword;
    }

    function validatePassword() {
        var pass2 = document.getElementById("repeatNewPassword").value;
        var pass1 = document.getElementById("newPassword").value;
        if (pass1 === pass2)
            document.getElementById("repeatNewPassword").setCustomValidity('');
        else
            document.getElementById("repeatNewPassword").setCustomValidity("<fmt:message key="password.confirmation" bundle="${bndl}"/>");

    }
</script>

<script>
    $(document).ready(function () {
        $('select').material_select();
    });

    $(window).on("load", function () {
        if ($('#loginError').length == 1) {
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