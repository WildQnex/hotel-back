<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="text" var="bndl"/>


<div class="center">
    <hr>
    <form name="Name" action="invokeServlet" method="POST">
        <input name="action" type="hidden" value="addApartment">
        <p><input name="class"></p>
        <p><input name="number"></p>
        <p>
            <button type="submit"><fmt:message key="admin.add.apartment.button" bundle="${bndl}"/></button>
        </p>
    </form>
</div>