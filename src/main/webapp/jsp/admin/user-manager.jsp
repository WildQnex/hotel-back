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

<jsp:include page="../header.jsp"/>

<main>
    <div class="container">
        <form class="col s12" name="login" action="booking" method="POST">
            <div class="row"></div>
            <div class="row">
                <div class="input-field col s3 m3">
                    <input id="password" type="text" class="validate" name="E-mail">
                    <label for="password">E-mail</label>
                </div>
                <button class="col s6 m2 offset-s3 offset-m5 btn waves-effect waves-light" type="submit">
                    Найти
                </button>
            </div>
        </form>
    </div>
    <div id="reservations" class="container">
        <div class="container">
            <ul class="collapsible popout" data-collapsible="accordion">
                <c:forEach items="${users}" var="user">
                    <li>
                        <div class="collapsible-header">
                            <i class="material-icons">blur_on</i>User: ${user.firstName} ${user.middleName} ${user.lastName}
                            Active: ${user.active}
                        </div>
                        <div class="collapsible-body">
                                <%--<div class="row"><strong>Order made on</strong> ${reservation.orderTime.toLocalDate()}--%>
                                <%--<strong>  at </strong> ${reservation.orderTime.toLocalTime()}</div>--%>
                                <%--<div class="row"><strong>Apartment number:</strong> ${reservation.apartment.number}</div>--%>
                                <%--<div class="row"><strong>Person amount:</strong> ${reservation.personAmount}</div>--%>
                                <%--<div class="row"><strong>Cost per person:</strong> ${reservation.costPerPerson} $</div>--%>
                                <%--<div class="row"><strong>Cost per night:</strong> ${reservation.costPerNight} $</div>--%>
                                <%--<div class="row"><strong>Total cost:</strong> ${reservation.totalCost} $</div>--%>
                                <%--<div class="row"><strong>Order status:</strong> ${reservation.status}</div>--%>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</main>


<jsp:include page="../footer.jsp"/>

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

<script>
    $( window ).on( "load", function() {
        if ( $('#login-error').length == 1){
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
