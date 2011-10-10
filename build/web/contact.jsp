<%-- 
    Document   : contact
    Created on : Sep 25, 2011, 6:43:22 PM
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
                <form action="Email" method="post" class="formUser" >
                    <p> Send us an email </p>
                    <div class="messageError">
                        <p>    ${fn:escapeXml(param.messageError)}</p>
                    </div>
                    <ul>
                        <li> <label> From: </label> <input type="text" name="from" />
                        <li> <label> Subject: </label> <input type="text" name="subject" />
                        <li> <label> Text </label> <textarea cols="40" rows="5" id="bodyEmail" name="bodyEmail"  > </textarea> </li>
                        <li> <input type="submit" value="Send" /> </li>
                    </ul>
                </form>
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
