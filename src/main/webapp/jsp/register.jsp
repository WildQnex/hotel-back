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
            <form class="col s12" name="register" action="invokeServlet" method="POST">
                <input name="action" type="hidden" value="register">
                <div class="row">
                    <div class="input-field col s6">
                        <input id="first_name" name="first_name" type="text" class="validate">
                        <label for="first_name">First Name</label>
                    </div>
                    <div class="input-field col s6">
                        <input id="last_name" name="last_name" type="text" class="validate">
                        <label for="last_name">Last Name</label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s6">
                        <input id="middle_name" name="middle_name" type="text" class="validate">
                        <label for="middle_name">Middle Name *</label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s6">
                        <input id="email" name="email" type="email" class="validate">
                        <label for="email">E-mail</label>
                    </div>
                    <div class="input-field col s6">
                        <input id="phone_number" name="phone_number" type="text" class="validate">
                        <label for="phone_number">Phone Number</label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s6">
                        <input id="password" name="password" type="password" class="validate">
                        <label for="password">Password</label>
                    </div>
                    <div class="input-field col s6">
                        <input id="repeat_password" name="repeat_password" type="password" class="validate">
                        <label for="repeat_password">Repeat Password</label>
                    </div>
                </div>

                <div class="row red-text">
                    * - Fields marked with this sign are optional
                </div>

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

<script>
    <c:if test="${not empty loginError}">
    $('#modal').modal('open');
    </c:if>
</script>


</html>
