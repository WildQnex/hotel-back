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

<div class="navbar-fixed">
    <nav class="white" role="navigation">

        <div class="nav-wrapper container ">
            <a href="booking?action=forward&page=main"
               class="brand-logo amber-text text-lighten-3 text-border"><fmt:message key="header.logo"
                                                                                     bundle="${bndl}"/></a>
        </div>

    </nav>
</div>

<main class="red-text">

    <h1 class="row"></h1>
    <h1 class="row"></h1>
    <h5><strong><ct:showMessage color="red" key="errorMessage"/></strong></h5>
    <h4 class="center">
        <a href="booking?action=forward&page=main"
           class="brand-logo center amber-text text-lighten-3 text-border">
            На главную
        </a>
    </h4>
</main>


<jsp:include page="footer.jsp"/>

</body>


<!--  Scripts-->
<script src="js/jquery-2.1.1.min.js"></script>
<script src="js/materialize.js"></script>
<script src="js/init.js"></script>


</html>

