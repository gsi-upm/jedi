<%-- 
    Document   : myAccount
    Created on : Jul 13, 2011, 6:51:35 PM
    Author     : nachomv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<jsp:useBean id="error" class="userdata.Error" scope="request" />
<jsp:useBean id="message" class="userdata.Error" scope="request" />



<c:if test="${!empty param.emailUpdated}">

    <c:set var="existsAt" value="${fn:contains(param.emailUpdated, '@')}" />
    <c:set var="existsDos" value="${fn:contains(param.emailUpdated, '.')}" />
    <c:set var="beforeAt" value="${fn:substringBefore(param.emailUpdated, '@')}" />
    <c:set var="afterAt" value="${fn:substringAfter(param.emailUpdated, '@')}" />
    <c:set var="sizeBefore" value="${fn:length(beforeAt)}" />
    <c:set var="sizeAfter" value="${fn:length(afterAt)}" />
    <c:set var="domainSufix" value="${fn:substringAfter(param.emailUpdated, '.')}" />
    <c:if test="${(!existsAt) || (!existsDos) || (sizeBefore == 0) || (sizeAfter) == 0 || (domainSufix == 0) }" var="emailOk" >
        <jsp:setProperty name="error" property="messageError" value="Email incorrect" />
    </c:if>
    <c:if test="${!emailOk}">
        
    
    <sql:update>
        UPDATE dataUsers
        set email = ?
        where user = ?
        <sql:param value="${param.emailUpdated}" />
        <sql:param value="${validUser.user}" />
    </sql:update>
        <jsp:setProperty name="message" property="messageError" value="Email updated correctly" />
        <jsp:setProperty name="validUser" property="email" value="${param.emailUpdated}" />
        </c:if>


</c:if>

<c:if test="${!empty param.passUpdated && !empty param.passUpdatedRepeat }" >
    <c:set var="passLength" value="${fn:length(param.passUpdated)}"/>
    <c:if test="${ passLength < 6 || passLength > 30}" var="lengthOk" >
        <jsp:setProperty name="error" property="messageError" value="Password must have between 6 and 30 characters" />
    </c:if>

    <c:if test="${param.passUpdated != param.passUpdatedRepeat}" var="repeatOk" >
        <jsp:setProperty name="error" property="messageError" value="Passwords do not match" />
    </c:if>

    <c:if test="${!lengthOk && !repeatOk}">
        <sql:update>
            UPDATE dataUsers
            set password = ?
            where user = ?
            <sql:param value="${param.passUpdated}" />
            <sql:param value="${validUser.user}" />
        </sql:update>
            <jsp:setProperty name="message" property="messageError" value="Password updated correctly" />
            <jsp:setProperty name="validUser" property="password" value="${param.passUpdated}" />
    </c:if>
</c:if>

<html>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>
            <c:if test="${validUser == null}" >
                <jsp:forward page="login.jsp">
                    <jsp:param name="origURL" value="${pageContext.request.requestURL}" />
                    <jsp:param name="messageError" value="Please log in first" />
                </jsp:forward>
            </c:if>

            <div id="main" class="round">
                 <div id="dataUpdate" class="formUser">
                <p> My Account </p>
                <ul>
                    <li> <label> Username: </label> ${validUser.user}  </li>
                    <li> <label> Email: </label> ${validUser.email} </li>
                </ul>
                


                
                <p> Update </p>

                <p> ${error.messageError} </p>
                <p> ${message.messageError} </p>

               
                    <form action="myAccount.jsp" method="post" name="updateInfo" >
                        <ul>
                            <li> <label> New email </label> <input type="text" name="emailUpdated"  />  </li>
                            <li> <label> New password </label> <input type="password" name="passUpdated" /> </li>
                            <li> <label> Rewrite new password </label> <input type="password" name="passUpdatedRepeat" /> </li>
                            <li> <input type="submit" value="update" /> </li>
                        </ul>
                    </form>
                </div>
            </div>

            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
