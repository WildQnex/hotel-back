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
    ${bookingError}
    <div class="container">
        <form class="col s12" name="book" action="booking" method="POST">
            <input type="hidden" name="action" value="book_apartment">
            <input type="hidden" name="apartmentId" value="${apartmentClass.id}">
            <div class="row">
                <div class="input-field col s4 m3">
                    <label for="check-in-date">Check In date</label>
                    <input name="checkInDate" id="check-in-date" type="text" class="datepicker">
                </div>
                <div class="input-field col s4 m3">
                    <label for="check-out-date">Check Out date</label>
                    <input name="checkOutDate" id="check-out-date" type="text" class="datepicker">
                </div>
                <div class="input-field col s4 m3">
                    <label for="personsAmount">Persons amount</label>
                    <input name="personsAmount" id="personsAmount" type="text">
                </div>
            </div>
            <div class="row">
                <button class="col s6 m2 offset-s3 offset-m5 btn waves-effect waves-light center" type="submit">
                    Book
                </button>
            </div>
        </form>
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