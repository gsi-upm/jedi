
<%--
    Document   : registersuccesful
    Created on : 02-jul-2011, 15:40:49
    Author     : nachomv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<sql:query var="gsiWeb">
    SELECT * FROM dataUsers
    WHERE user = ?
    <sql:param value="${param.user}" />
</sql:query>
<c:choose>
    <c:when test="${gsiWeb.rowCount == 0}">
        <sql:update>
            INSERT INTO dataUsers
            (user, email, password, question, answer) VALUES (?,?,?,?,?)
            <sql:param value="${param.user}" />
            <sql:param value="${param.email}" />
            <sql:param value="${param.password}" />
            <sql:param value="${param.question}" />
            <sql:param value="${param.answer}" />
        </sql:update>
    </c:when>
    <c:otherwise>
        <sql:update>
            UPDATE dataUsers
            SET email = ?, password = ?, question = ?, answer = ? WHERE USER = ?
            <sql:param value="${param.email}" />
            <sql:param value="${param.password}" />
            <sql:param value="${param.user}" />
            <sql:param value="${param.question}" />
            <sql:param value="${param.answer}" />
        </sql:update>
    </c:otherwise>
</c:choose>
<sql:query var="gsiWeb" scope="session">
    select * FROM dataUsers
    WHERE user = ?
    <sql:param value="${param.user}" />
</sql:query>


<html>
    <%@include file="/WEB-INF/jspf/tagCloud.jspf" %>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>
            <div id="main" class="round">
                <p> Hello ${fn:escapeXml(param.user)}, You have been registered succesfully.
                    Click <a href="<c:url value="login.jsp" /> "> here </a>  to log in  </p>
                <p>
                    <sql:query var="gsiWeb" scope="session" >
                        SELECT * FROM dataUsers
                        WHERE user = ?
                        <sql:param value="${param.user}" />
                    </sql:query>



                </p>
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
