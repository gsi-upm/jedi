<%-- 
    Document   : deleteUser
    Created on : Sep 12, 2011, 11:24:29 AM
    Author     : nachomv
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--<sql:query var="gsiWeb">
    SELECT * FROM dataUsers
    WHERE user = ? and email = ?
    <sql:param value="${validUser.user}" />
    <sql:param value="${validUser.email}" />
</sql:query>--%>

<sql:query var="deleteUser">
    select * from dataUsers
    WHERE user = ? AND email = ?
    <sql:param value="${validUser.user}" />
    <sql:param value="${validUser.email}" />
</sql:query>
    <c:if test="{deleteUser.rowCount != 0}">
        <sql:query var="delete">
            ALTER table dataUsers drop deleteUser
        </sql:query>
    </c:if>




<html>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>
            <div id="main" class="round">
                <p> Your data has been erased correctly </p>
               <c:remove var="validUser" />

            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
