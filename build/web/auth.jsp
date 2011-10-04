<%-- 
    Document   : auth
    Created on : Jul 8, 2011, 3:01:30 AM
    Author     : nachomv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:remove var="validUser" />

<c:if test="${empty param.user || param.password}">
    <c:redirect url="login.jsp">
        <c:param name="messageError" value="Please, enter and User name and a Password." />
    </c:redirect>
</c:if>

<sql:query var="userInfo">
    select * from dataUsers
    where user = ? AND  password = ?
    <sql:param value="${param.user}" />
    <sql:param value="${param.password}" />
</sql:query>

<c:if test="${userInfo.rowCount == 0}">
    <c:redirect url="login.jsp">
        <c:param name="messageError" value="The User name or Password you entered is not valid." />
    </c:redirect>
</c:if>
    
<c:set var="dbValues" value="${userInfo.rows[0]}" />
<jsp:useBean id="validUser" scope="session" class="userdata.User">
    < <jsp:setProperty name="validUser" property="user" value="${dbValues.User}" />
    <jsp:setProperty name="validUser" property="email" value="${dbValues.Email}" />
    <jsp:setProperty name="validUser" property="password" value="${dbValues.Password}" />
</jsp:useBean>



<c:choose>
    <c:when test="${!empty param.origUrl}">
        <c:redirect url="${param.origUrl}" />
    </c:when>
    <c:otherwise>
        <c:redirect url="main.jsp" />
    </c:otherwise>
</c:choose>



<html>
    <%@include file="/WEB-INF/jspf/tagCloud.jspf" %>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>
            
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
