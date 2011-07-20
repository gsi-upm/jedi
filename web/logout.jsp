<%-- 
    Document   : logout
    Created on : Jul 13, 2011, 6:25:53 PM
    Author     : nachomv
--%>


    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

    <html>
    <div class="container">
    <head>
       <%@include file="/WEB-INF/jspf/header.jspf" %>
        <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
    </head>

      <body>
        <div id="main" class="round">
    <% session.invalidate(); %>
    <c:redirect url="index.jsp" />
        </div>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    </div>
    </html>
