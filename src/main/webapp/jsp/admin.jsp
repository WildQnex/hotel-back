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
        <ul class="collapsible popout" data-collapsible="accordion">
            <c:forEach items="${reservations}" var="reservation">
                <li>
                    <div class="collapsible-header"><i class="material-icons">blur_on</i>${reservation.id} order</div>
                    <div class="collapsible-body">
                        <div class="row"> ${reservation.orderTime}, order status - ${reservation.status}</div>
                        <div class="divider"></div>
                        <div class="row"></div>
                        <form class="col s12" name="login" action="booking" method="POST">
                            <div class="row">
                                <div class="input-field col s6 m3 offset-m2">
                                    <select>
                                        <option value="" disabled>Choose apartmet</option>
                                        <option selected name="apartmentId" value="${reservation.apartment.id}">${reservation.apartment.number}</option>
                                        <c:forEach items="${freeApartments.get(reservation)}" var="apartment">
                                            <option value="${apartment.id}">${apartment.number}</option>
                                        </c:forEach>
                                    </select>
                                    <label>Apartmet Number</label>
                                </div>
                                <div class="input-field col s6 m3 offset-m2">
                                    <select>
                                        <option value="" disabled>Choose status</option>
                                        <option selected name="apartmentId" value="Approve">Approve</option>
                                        <option value="Decline">Decline</option>
                                    </select>
                                    <label>Apartmet Status</label>
                                </div>
                            </div>
                            <div class="row">
                                <button class="col s2 m2 offset-s5 offset-m5 btn waves-effect waves-light center" type="submit">Send</button>
                            </div>
                        </form>
                    </div>
                </li>
            </c:forEach>
        </ul>
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

<script>
    <c:if test="${not empty loginError}">
    $('#modal').modal('open');
    </c:if>
</script>

<script>
    $(document).ready(function () {
        $('select').material_select();
    })
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