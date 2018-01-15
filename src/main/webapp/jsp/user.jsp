<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="text" var="bndl"/>
<body>
<div style="float: left; margin-left: 15%;">
    <c:forEach items="${apartments}" var="item">
        ${item.number}-${item.apartmentClass.toString()} | <a
            href="invokeServlet?action=book&id=${item.id}"><fmt:message key="book.book" bundle="${bndl}"/></a> <br>
    </c:forEach>
</div>
<div style="float: right; margin-right: 15%;">
    <div class="center">Your books</div>
    <c:forEach items="${booked}" var="item">
        ${item.number}-${item.apartmentClass.toString()} | <a
            href="invokeServlet?action=unbook&id=${item.id}"><fmt:message key="book.unbook" bundle="${bndl}"/></a> <br>
    </c:forEach>
</div>
</body>

