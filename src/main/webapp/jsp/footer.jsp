<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="text" var="bndl"/>

<footer class="page-footer amber accent-4">
    <div class="container">
        <div class="row">
            <div class="col s12 center">
                <h5 class="white-text"><fmt:message key="footer.company" bundle="${bndl}"/></h5>
                <p class="grey-text text-lighten-4 footer-margin"><fmt:message key="footer.company.name"
                                                                               bundle="${bndl}"/></p>
                <p class="grey-text text-lighten-4 footer-margin"><fmt:message key="footer.text" bundle="${bndl}"/></p>
                <p class="grey-text text-lighten-4 footer-margin"><fmt:message key="footer.address"
                                                                               bundle="${bndl}"/></p>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container center">
            <fmt:message key="footer.copyright" bundle="${bndl}"/>
        </div>
    </div>
</footer>