<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="text" var="bndl"/>

<header>
    <div class="navbar-fixed">
        <nav class="white" role="navigation">

            <div class="nav-wrapper container">
                <a id="logo-container" href="booking?action=forward&page=main"
                   class="brand-logo"><fmt:message key="header.logo" bundle="${bndl}"/></a>
                <ul class="right hide-on-med-and-down">

                    <li><a href="booking?action=show_apartment_classes">
                        <fmt:message key="header.apartments" bundle="${bndl}"/></a>
                    </li>

                    <li><a class='dropdown-button' href='#' data-beloworigin="true" data-activates='dropdown-language'>
                        <fmt:message key="language.name" bundle="${bndl}"/><i
                            class="material-icons right">arrow_drop_down</i>
                    </a></li>

                    <li>
                        <i class="material-icons header-icon">account_box</i>
                    </li>


                    <c:if test="${empty user}">
                        <li><a class="modal-trigger" href="#modal"><fmt:message key="login.button"
                                                                                bundle="${bndl}"/></a>
                        </li>
                        <li><a href="booking?action=forward&page=register"><fmt:message
                                key="register.button"
                                bundle="${bndl}"/></a>
                        </li>
                    </c:if>

                    <c:if test="${not empty user}">


                        <li><a class='dropdown-button' href='#' data-beloworigin="true" data-activates='dropdown-user'>
                            <c:out value="${user.firstName}"/><i class="material-icons right">arrow_drop_down</i>
                        </a></li>
                        <c:if test="${user.role.toString().equalsIgnoreCase('Admin')}">
                            <li><a class='dropdown-button' href='#' data-beloworigin="true" data-activates='dropdown-admin'>
                               Admin<i class="material-icons right">arrow_drop_down</i>
                            </a></li>
                        </c:if>

                    </c:if>
                </ul>

                <ul id="nav-mobile" class="side-nav">
                    <li><a class="modal-trigger" href="#modal1"><fmt:message key="login.button" bundle="${bndl}"/></a>
                    </li>
                    <li><a class='dropdown-button' href='#' data-beloworigin="true"
                           data-activates='dropdown-language-mobile'>
                        <fmt:message key="language.name" bundle="${bndl}"/><i
                            class="material-icons right">arrow_drop_down</i>
                    </a></li>
                </ul>

                <a href="#" data-activates="nav-mobile" class="button-collapse"><i class="material-icons">menu</i></a>
            </div>

        </nav>
    </div>
    <div id="modal" class="modal">
        <div class="modal-content">

            <form class="col s12" name="login" action="booking" method="POST">

                <div class="row">
                    <div class="col s6 offset-s3 center"><span class="flow-text"><fmt:message key="login.auth"
                                                                                              bundle="${bndl}"/></span>
                    </div>
                </div>

                <input name="action" type="hidden" value="login">

                <div class="row">
                    <div class="input-field col s8 m6 offset-m3 offset-s2">
                        <input id="username" type="text" name="mail" class="validate">
                        <label for="username"><fmt:message key="login.mail" bundle="${bndl}"/></label>
                    </div>
                </div>

                <div class="row">
                    <div class="input-field col s8 m6 offset-m3 offset-s2">
                        <input id="password" type="password" class="validate" name="password">
                        <label for="password"><fmt:message key="login.password" bundle="${bndl}"/></label>
                    </div>
                </div>

                <c:if test="${not empty loginError}">
                    <div class="row">
                        <div class="col s8 m6 offset-m3 offset-s2 center red-text">${loginError}</div>
                    </div>
                </c:if>

                <div class="row">
                    <button class="col s6 m2 offset-s3 offset-m5 btn waves-effect waves-light center" type="submit">
                        <fmt:message key="login.button" bundle="${bndl}"/>
                    </button>
                </div>
            </form>
        </div>
    </div>


</header>

<ul id='dropdown-user' class='dropdown-content'>
    <li><a href="booking?action=forward&page=user">Профиль</a></li>
    <li><a href="booking?action=show_personal_reservations">Заказы</a></li>
    <li class="divider"></li>
    <li><a href="booking?action=logout"><fmt:message key="header.logout"
                                                     bundle="${bndl}"/></a>
    </li>
</ul>
<ul id='dropdown-admin' class='dropdown-content'>
    <li><a href="booking?action=show_admin_page">Заказы</a></li>
    <li><a href="booking?action=show_apartment_editor">Номера</a></li>
</ul>

<ul id='dropdown-language' class='dropdown-content'>
    <li><a href="booking?action=set_locale&value=ru_RU">Русский</a></li>
    <li class="divider"></li>
    <li><a href="booking?action=set_locale&value=en_US">English</a></li>
</ul>

<ul id='dropdown-language-mobile' class='dropdown-content'>
    <li><a href="booking?action=set_locale&value=ru_RU">Русский</a></li>
    <li class="divider"></li>
    <li><a href="booking?action=set_locale&value=en_US">English</a></li>
</ul>
