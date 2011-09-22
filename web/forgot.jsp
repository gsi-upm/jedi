<%-- 
    Document   : forgot
    Created on : Sep 22, 2011, 7:15:13 PM
    Author     : nachomv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <%@include file="/WEB-INF/jspf/tagCloud.jspf" %>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>
        <body>

            <div id="main" class="round">
                <p> ${fn:escapeXml(param.messageError)} </p>
                <p> ${fn:escapeXml(param.newPassword)} </p>
                <p> Please introduce your username or your email </p>
                <div id="forgotPass" class="formUser">
                    <form action="Database?action=forgotPass" method="post" name="recoverPass" >
                        <ul>
                            <li> <label> Username </label> <input type="text" name="username" /> </li>
                            <li> <label> Email </label> <input type="text" name="email" /> </li>
                            <li> <input type="hidden" name="forgotPass" /> </li>
                            <li> <input type="submit" value="Get a new password" /> </li>
                        </ul>
                    </form>
                </div>
            </div>

            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>

</html>
