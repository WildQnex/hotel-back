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
        <c:if test="${not empty approve_reservation_error}">
            <div class="row"></div>
            <div class="row">
                <div class="col s8 m6 offset-m3 offset-s2 center red-text">${approve_reservation_error}</div>
            </div>
            <c:remove var="approve_reservation_error" scope="session"/>
        </c:if>
        <ul class="collapsible popout" data-collapsible="accordion">
            <c:forEach items="${reservations}" var="reservation">
                <li>
                    <div class="collapsible-header"><i class="material-icons">blur_on</i>Order № ${reservation.id}</div>
                    <div class="collapsible-body">
                        <div class="row">Reservation period: ${reservation.checkInDate}
                            - ${reservation.checkOutDate}</div>
                        <div class="row">Order made on ${reservation.orderTime.toLocalDate()}
                            in ${reservation.orderTime.toLocalTime()}</div>
                        <div class="row">Total cost: ${reservation.totalCost}</div>
                        <div class="row">Order status: ${reservation.status}</div>
                        <div class="divider"></div>
                        <div class="row"></div>
                        <input name="action" type="hidden" value="approve_reservation">
                        <div class="row">
                            <div class="input-field col s6 m4 offset-m4 offset-s3">
                                <select id="apartment_id" >
                                    <option value="" disabled>Choose apartment</option>
                                    <option selected name="apartment_id"
                                            value="${reservation.apartment.id}">${reservation.apartment.number}</option>
                                    <c:forEach items="${freeApartments.get(reservation)}" var="apartment">
                                        <option value="${apartment.id}">${apartment.number}</option>
                                    </c:forEach>
                                </select>
                                <label>Apartment Number</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s6 m3 offset-m2">
                                <a id="approve"
                                   href="booking?action=approve_reservation&reservation_id=${reservation.id}&status=approved&apartment_id=">
                                    <button id="approve_button" onclick="$(this).setApprovedId();" class="btn waves-effect waves-light center">
                                        Approve
                                    </button>
                                </a>
                            </div>
                            <div class="col s6 m3 offset-m2">
                                <a id="decline"
                                   href="booking?action=approve_reservation&reservation_id=${reservation.id}&status=declined&apartment_id=${reservation.apartment.id}">
                                    <button id="decline_button" class="btn waves-effect waves-light center">
                                        Decline
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
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
            endingTop: '10%' // Ending top style attribute
        }
    );
</script>

<script>
    $(window).on("load", function () {
        if ($('#login-error').length == 1) {
            $('#modal').modal('open');
        }
    });
</script>

<script>
    $(document).ready(function () {
        $('select').material_select();
    });

    $.fn.setApprovedId = function() {
        var collapsibleDiv = this.parent().parent().parent().parent();
        var newHref = this.parent().attr('href') + collapsibleDiv.find('select').val();
        this.parent().attr('href', newHref);
    };
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