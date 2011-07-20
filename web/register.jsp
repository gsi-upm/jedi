<%-- 
    Document   : register
    Created on : 04-jul-2011, 12:04:15
    Author     : imendizabal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" import="userdata.Error"%>

<html>
    <div class="container">
    <head>
       <%@include file="/WEB-INF/jspf/header.jspf" %>
        <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
    </head>

      <body>
        <div id="main" class="round">
            <div id="register" class="formUser">

         <c:if test="${empty error.messageError}" var="noError">
              <%-- <p> Register </p>--%>
           </c:if>

               <div class="messageError">
           <c:if test="${!noError}">
               <p> ${error.messageError} </p>
           </c:if>
               </div>                
                <form action="validate.jsp" method="post" name="register">
                <ul>                    
                        <li> <label> User </label> <input type="text" name="user" value="${param.user}" />  </li>
                        <li> <label> Email </label> <input type="text" name="email" value="${param.email}" /> </li>
                        <li> <label> Password </label> <input type="password" name="password"/> </li>
                        <li> <label> Repeat password </label> <input type="password" name="repeatPassword"/></li>
                        <li> <input type="submit" value="register" name="send" /> </li>
                </ul>
                </form>
            </div>        
        </div>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    </div>
</html>
