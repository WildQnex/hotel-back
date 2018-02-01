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
    <div id="reservations" class="container">
        <div class="container">

            <ul class="collapsible popout" data-collapsible="accordion">
                <c:forEach items="${reservations}" var="reservation">
                    <li>
                        <div class="collapsible-header">
                            <i class="material-icons">blur_on</i><fmt:message key="reservation.order"
                                                                              bundle="${bndl}"/> ${reservation.checkInDate}
                            - ${reservation.checkOutDate} <fmt:message key="reservation.status" bundle="${bndl}"/>
                            : ${reservation.status}
                        </div>
                        <div class="collapsible-body">
                            <div class="row"><strong><fmt:message key="reservation.order.made.on"
                                                                  bundle="${bndl}"/></strong> ${reservation.orderTime.toLocalDate()}
                                <strong> <fmt:message key="reservation.order.made.at"
                                                      bundle="${bndl}"/> </strong> ${reservation.orderTime.toLocalTime()}
                            </div>
                            <div class="row"><strong><fmt:message key="apartment.number"
                                                                  bundle="${bndl}"/>:</strong> ${reservation.apartment.number}
                            </div>
                            <div class="row"><strong><fmt:message key="apartment.class.person.amount" bundle="${bndl}"/>:</strong> ${reservation.personAmount}
                            </div>
                            <div class="row"><strong><fmt:message key="apartment.cost.per.person"
                                                                  bundle="${bndl}"/>:</strong> ${reservation.costPerPerson}
                                $
                            </div>
                            <div class="row"><strong><fmt:message key="apartment.cost.per.night"
                                                                  bundle="${bndl}"/>:</strong> ${reservation.costPerNight}
                                $
                            </div>
                            <div class="row"><strong><fmt:message key="apartment.total.cost"
                                                                  bundle="${bndl}"/>:</strong> ${reservation.totalCost}
                                $
                            </div>
                            <div class="row"><strong><fmt:message key="reservation.order.status"
                                                                  bundle="${bndl}"/>:</strong> ${reservation.status}
                            </div>
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