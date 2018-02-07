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
    <div id="index-banner" class="parallax-container">
        <div class="section no-pad-bot">
            <div class="container">
                <br><br>
                <h1 class="header center  amber-text text-lighten-3 text-border"><fmt:message key="main.hotel"
                                                                                              bundle="${bndl}"/></h1>
                <div class="row center">
                    <h5 class="header col s12 light amber-text text-lighten-3 text-border"><fmt:message
                            key="main.hotel.stars" bundle="${bndl}"/></h5>
                </div>
                <br><br>

            </div>
        </div>
        <div class="parallax"><img src="img/main-page.jpg" alt="Unsplashed background img 1"></div>
    </div>


    <div class="container">
        <div class="section">

            <!--   Icon Section   -->
            <div class="row">
                <div class="col s12 m4">
                    <div class="icon-block">
                        <h2 class="center brown-text"><i class="material-icons">account_balance</i></h2>
                        <h5 class="center"><fmt:message key="main.building" bundle="${bndl}"/></h5>

                        <p class="light"><fmt:message key="main.building.description" bundle="${bndl}"/></p>
                    </div>
                </div>

                <div class="col s12 m4">
                    <div class="icon-block">
                        <h2 class="center brown-text"><i class="material-icons">group</i></h2>
                        <h5 class="center"><fmt:message key="main.staff" bundle="${bndl}"/></h5>

                        <p class="light"><fmt:message key="main.staff.description" bundle="${bndl}"/></p>
                    </div>
                </div>

                <div class="col s12 m4">
                    <div class="icon-block">
                        <h2 class="center brown-text"><i class="material-icons">store</i></h2>
                        <h5 class="center"><fmt:message key="main.apartments" bundle="${bndl}"/></h5>

                        <p class="light"><fmt:message key="main.apartments.description" bundle="${bndl}"/></p>
                    </div>
                </div>
            </div>

        </div>
    </div>


    <div class="parallax-container valign-wrapper brown-text text-darken-3">
        <div class="section no-pad-bot">
            <div class="container">
                <div class="row center">
                    <h5 class="header col s12 amber-text text-lighten-3 text-border"><fmt:message key="main.restaurant"
                                                                                                  bundle="${bndl}"/></h5>
                </div>
            </div>
        </div>
        <div class="parallax"><img src="img/main-restaurant.jpg" alt="Unsplashed background img 2"></div>
    </div>

</main>

<jsp:include page="footer.jsp"/>

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


</body>
</html>
