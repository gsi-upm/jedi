<%-- 
    Document   : upload
    Created on : 04-jul-2011, 11:21:30
    Author     : imendizabal
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">




<%@page language="java" import="java.util.*" %>
<%@page errorPage="error.jsp" %>

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
         
          
        <div id="main" class="round" >
            <p> Upload Capabilities </p>
            <form method="post" action="UploadData" name="upload" enctype="multipart/form-data" class="formUser" >
                <ul>
                    <li> <label> Name </label> <input type="text" name="nameCap" /> </li>
                    <li> <label> Description </label> <textarea cols="40" rows="5" name="comments"> </textarea> </li>
                    <li> <label> Select a file to upload (Max size: 10MB)</label> <input type="file" name="uploadfile" /> </li>
                    <li> <input type="hidden" name="todo" value="upload" /> </li
                    <li> <input type="submit" value="Upload" /> </li>
                </ul>
            </form>

</div>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    </div>
</html>
