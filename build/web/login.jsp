<%-- 
    Document   : login
    Created on : 04-jul-2011, 12:07:56
    Author     : imendizabal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <div class="container">
    <head>
       <%@include file="/WEB-INF/jspf/header.jspf" %>
        <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
    </head>

      <body>
        <div id="main" class="round">

            <div class="messageError">
                <p>    ${fn:escapeXml(param.messageError)}</p>
            </div>

                <div id="login" class="formUser">
                <form action="auth.jsp" method="post" name="login">
                <ul>
                    <li> <label> User </label> <input type="text" name="user" value="${fn:escapeXml(cookie.user.value)}" />  </li>
                    <li> <label> Password </label> <input type="password" name="password" value="${fn:escapeXml(cookie.password.value)}" /> </li>
                    <li> <label> Remember my user and password </label> <input type="checkbox" name="remember" ${!empty cookie.user ? 'checked' : ''}> </li>
                    <li> <p> (This feature requires cookies to be enabled in your browser) </p> </li>
                    <li> <input type="submit" value="login" />
                </ul>
                </form>
            </div>
        </div>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    </div>
</html>
