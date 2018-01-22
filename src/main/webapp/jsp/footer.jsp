<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="text" var="bndl"/>

<footer class="page-footer teal">
    <div class="container">
        <div class="row">
            <div class="col s12">
                <h5 class="white-text center">Company Bio</h5>
                <p class="grey-text text-lighten-4">We are a team of college students working on this project like it's
                    our full time job. Any amount would help support and continue development on this project and is
                    greatly appreciated.</p>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container center">
            All rights reserved. © Vadim Martyniuk, 2017-2018
        </div>
    </div>
</footer>