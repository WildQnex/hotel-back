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
        <ul class="collapsible popout" data-collapsible="accordion">
            <c:forEach items="${apartments}" var="apartment">
            <li>
                <div class="collapsible-header">
                    <i class="material-icons">blur_on</i>№ ${apartment.number} Class
                    : ${apartment.apartmentClass.type}
                </div>

                <div class="collapsible-body">
                    <form class="col s12" name="book" action="booking" method="POST">
                        <input name="action" value="editApartment" type="hidden"/>
                        <input name="type" value="update" type="hidden"/>
                        <input name="apartmentId" value="${apartment.id}" type="hidden">
                        <div class="row">
                            <div class="input-field col s6">
                                <input id="number" name="number" type="text" class="validate"
                                       value="${apartment.number}">
                                <label for="number">Apartment Number</label>
                            </div>
                            <div class="input-field col s6">
                                <input id="floor" name="floor" type="text" class="validate" value="${apartment.floor}">
                                <label for="floor">Floor</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s6 m4 offset-m4 offset-s3">
                                <select id="class" name="class">
                                    <option value="" disabled>Choose apartment class</option>
                                    <option selected name="class"
                                            value="${apartment.apartmentClass.id}">${apartment.apartmentClass.type}</option>
                                    <c:forEach items="${apartmentClasses}" var="apartmentClass">
                                        <option value="${apartmentClass.id}">${apartmentClass.type}</option>
                                    </c:forEach>
                                </select>
                                <label>Apartment Class</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s6 m3 offset-m2">
                                <button class="btn waves-effect waves-light" type="submit">
                                    Edit
                                </button>
                            </div>
                            <div class="col s6 m3 offset-m2">
                                <a href="booking?action=edit_apartment&apartmentId=${apartment.id}&type=delete">
                                    <button class="btn waves-effect waves-light" type="button">
                                        Delete
                                    </button>
                                </a>
                            </div>
                        </div>
                    </form>
                </div>

            </li>
            </c:forEach>
            <div class="row"></div>
            <div class="row center">
                <a class="modal-trigger" href="#modalApartment">
                    <button class="btn waves-effect waves-light center">
                        Add apartment
                    </button>
                </a>
            </div>
    </div>
    </ul>
</main>

<jsp:include page="../footer.jsp"/>

</body>

<div id="modalApartment" class="modal">
    <div class="modal-content">

        <form class="col s12" action="booking" method="POST">

            <div class="row">
                <div class="col s6 offset-s3 center"><span class="flow-text">Add apartment</span>
                </div>
            </div>

            <input name="action" type="hidden" value="addApartment">

            <div class="row">
                <div class="input-field col s8 m6 offset-m3 offset-s2">
                    <input id="apartmentNumber" type="text" name="apartmentNumber" class="validate">
                    <label for="apartmentNumber">Number</label>
                </div>
            </div>

            <div class="row">
                <div class="input-field col s8 m6 offset-m3 offset-s2">
                    <input id="apartmentFloor" type="text" class="validate" name="apartmentFloor">
                    <label for="apartmentFloor">Floor</label>
                </div>
            </div>

            <div class="row">
                <div class="input-field col s8 m6 offset-m3 offset-s2">
                    <select name="apartmentClass">
                        <option value="" disabled>Choose apartment class</option>
                        <c:forEach items="${apartmentClasses}" var="apartmentClass">
                            <option selected value="${apartmentClass.id}">${apartmentClass.type}</option>
                        </c:forEach>
                    </select>
                    <label>Apartment Class</label>
                </div>
            </div>

            <div class="row">
                <button class="col s6 m2 offset-s3 offset-m5 btn waves-effect waves-light center" type="submit">
                    Create
                </button>
            </div>
        </form>
    </div>
</div>
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
    $('.modalApartment').modal({
            dismissible: true, // Modal can be dismissed by clicking outside of the modal
            opacity: .5, // Opacity of modal background
            inDuration: 300, // Transition in duration
            outDuration: 200, // Transition out duration
            startingTop: '4%', // Starting top style attribute
            endingTop: '10%', // Ending top style attribute
        }
    );

    $(document).ready(function () {
        $('select').material_select();
    });
</script>

<script>
    $(window).on("load", function () {
        if ($('#login-error').length == 1) {
            $('#modal').modal('open');
        }
    });
</script>


</html>
