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
    <div class="container">
        <ct:showMessage color="red" key="approveReservationError"/>

        <ul class="collapsible popout" data-collapsible="accordion">
            <c:forEach items="${reservations}" var="reservation">
                <li>
                    <div class="collapsible-header"><i class="material-icons">blur_on</i><fmt:message
                            key="reservation.order" bundle="${bndl}"/> № ${reservation.id}</div>
                    <div class="collapsible-body">
                        <div class="row"><fmt:message key="reservation.period"
                                                      bundle="${bndl}"/>: ${reservation.checkInDate}
                            - ${reservation.checkOutDate}</div>
                        <div class="row"><fmt:message key="reservation.order.made.on"
                                                      bundle="${bndl}"/> ${reservation.orderTime.toLocalDate()}
                            <fmt:message key="reservation.order.made.at"
                                         bundle="${bndl}"/> ${reservation.orderTime.toLocalTime()}</div>
                        <div class="row"><fmt:message key="apartment.total.cost"
                                                      bundle="${bndl}"/>: ${reservation.totalCost}</div>
                        <div class="row"><fmt:message key="reservation.order.status" bundle="${bndl}"/>:
                            <fmt:message key="reservation.status.${reservation.status}" bundle="${bndl}"/></div>
                        <div class="divider"></div>
                        <div class="row"></div>
                        <div class="row">
                            <div class="input-field col s6 m4 offset-m4 offset-s3">
                                <select id="apartmentId">
                                    <option value="" disabled><fmt:message key="apartment.choose"
                                                                           bundle="${bndl}"/></option>
                                    <option selected name="apartmentId"
                                            value="${reservation.apartment.id}">${reservation.apartment.number}</option>
                                    <c:forEach items="${freeApartments.get(reservation)}" var="apartment">
                                        <option value="${apartment.id}">${apartment.number}</option>
                                    </c:forEach>
                                </select>
                                <label><fmt:message key="apartment.number" bundle="${bndl}"/></label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s6 m3 offset-m2">
                                <a id="approve"
                                   href="booking?action=approve_reservation&reservationId=${reservation.id}&status=approved&apartmentId=">
                                    <button id="approveButton" onclick="$(this).setApprovedId();"
                                            class="btn amber accent-4 waves-effect waves-light center">
                                        <fmt:message key="apartment.approve" bundle="${bndl}"/>
                                    </button>
                                </a>
                            </div>
                            <div class="col s6 m3 offset-m2">
                                <a id="decline"
                                   href="booking?action=approve_reservation&reservationId=${reservation.id}&status=declined&apartmentId=${reservation.apartment.id}">
                                    <button id="declineButton" class="btn amber accent-4 waves-effect waves-light center">
                                        <fmt:message key="apartment.decline" bundle="${bndl}"/>
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
        if ($('#loginError').length == 1) {
            $('#modal').modal('open');
        }
    });
</script>

<script>
    $(document).ready(function () {
        $('select').material_select();
    });

    $.fn.setApprovedId = function () {
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