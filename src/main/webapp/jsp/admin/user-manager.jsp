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
    <link href="css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="css/app-style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>

</head>

<body>

<jsp:include page="../header.jsp"/>

<main>
    <div class="container">
    </div>
    <div id="reservations" class="container">
        <div class="container">
            <ul class="collapsible popout s12" data-collapsible="accordion">
                <c:forEach items="${users}" var="user">
                    <li>
                        <div class="collapsible-header">
                            <c:choose>
                                <c:when test="${user.active}">
                                    <i class="material-icons green-text">blur_on</i>
                                </c:when>
                                <c:otherwise>
                                    <i class="material-icons red-text">blur_on</i>
                                </c:otherwise>
                            </c:choose>
                                ${user.firstName} ${user.middleName} ${user.lastName}


                        </div>
                        <div class="collapsible-body">
                            <div class="row">
                                <div class="col m4 offset-m1">
                                    <a href="booking?action=admin_show_user_profile&id=${user.id}">
                                        <button class="btn amber accent-4 waves-effect waves-light">
                                            <fmt:message key="user.edit.profile" bundle="${bndl}"/>
                                        </button>
                                    </a>
                                </div>
                                <div class="col m4 offset-m3">
                                    <a href="booking?action=admin_show_user_reservations&id=${user.id}">
                                        <button class="btn amber accent-4 waves-effect waves-light">
                                            <fmt:message key="user.show.reservation" bundle="${bndl}"/>
                                        </button>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="center">
                <i class="material-icons green-text">blur_on</i> - <fmt:message key="user.active" bundle="${bndl}"/>,
                <i class="material-icons red-text">blur_on</i> - <fmt:message key="user.banned" bundle="${bndl}"/>
            </div>

        </div>
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

<script>
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
