<%-- 
    Document   : main
    Created on : Jul 8, 2011, 8:31:44 PM
    Author     : nachomv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>



<%@include file="/WEB-INF/jspf/tagCloud.jspf" %> 
   <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>


        <body>


            <div id="main" class="round">
                
                   
                




                <c:if test="${validUser == null}">
                    <jsp:forward page="login.jsp">
                        <jsp:param name="origURL" value="${pageContext.request.requestURL}" />
                        <jsp:param name="messageError" value="Please log in first." />
                    </jsp:forward>
                </c:if>

                <c:if test="$pageContext.request.method != 'POST'}" >
                    <c:redirect url="index.jsp" />
                </c:if>
                <p> Welcome, ${fn:escapeXml(validUser.user)}
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
