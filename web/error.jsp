<%--
    Document   : index
    Created on : 02-jul-2011, 15:40:49
    Author     : nachomv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page isErrorPage="true" %>


<html>
    <%@include file="/WEB-INF/jspf/tagCloud.jspf" %>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>
            <div id="main" class="round">
                <p> Error  </p>
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
