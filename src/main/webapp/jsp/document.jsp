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
    <div class="row">
        <a href="localhost:9000/api/v1/certificates/xlsCertificate">XLS Certificate</a>
    </div>
    <div class="row">
        <a href="localhost:9000/api/v1/certificates/pdfCertificate/3">PDF Certificate</a>
    </div>
    <div class="row">
        <a href="localhost:9000/api/v1/certificates/csvCertificate/1">CSV Certificate</a>
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
