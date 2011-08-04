<%--
    Document   : uploadCorrect
    Created on : Jul 22, 2011, 4:25:34 PM
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
           <c:if test="${validUser == null}" >
              <jsp:forward page="login.jsp">
                  <jsp:param name="origURL" value="${pageContext.request.requestURL}" />
                  <jsp:param name="messageError" value="Please log in first" />
              </jsp:forward>

          </c:if>
        <div id="main" class="round">
    <p> Download more files </p>
        </div>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    </div>
    </html>