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

<jsp:include page="header.jsp"/>

<main>
    <div class="container">
        <c:if test="${not empty apartment_classes_error}">
            <div class="row"></div>
            <div class="row">
                <div class="col s8 m6 offset-m3 offset-s2 center red-text">${apartment_classes_error}</div>
            </div>
            <c:remove var="apartment_classes_error" scope="session"/>
        </c:if>
        <div class="row">
            <c:forEach items="${apartmentClasses}" var="apartmentClass">
                <div class="col s6 m4">
                    <div class="card">
                        <div class="card-image">
                            <img class="class-img-height" src="${apartmentClass.imagePath}">
                            <span class="card-title">${apartmentClass.type}</span>
                        </div>
                        <div class="card-content class-content-height">
                            ${apartmentClass.description}
                        </div>
                        <div class="card-action center">
                            <a href="booking?action=show_apartment_class&id=${apartmentClass.id}"><fmt:message key="classes.show" bundle="${bndl}"/></a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</main>

<jsp:include page="footer.jsp"/>

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
    $( window ).on( "load", function() {
        if ( $('#login-error').length == 1){
            $('#modal').modal('open');
        }
    });
</script>


</html>
