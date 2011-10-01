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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

    <script type="text/javascript" src="js/jquery.min.js"> </script>
    <script type="text/javascript" src="js/jquery.tokeninput.js"> </script>


    <%--<%@include file="/WEB-INF/jspf/tagCloud.jspf" %>--%>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
                <link href="css/token-input-facebook.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>



            <c:if test="${validUser == null}" >
                <jsp:forward page="login.jsp">
                    <jsp:param name="origUrL" value="${pageContext.request.requestURL}" />
                    <jsp:param name="messageError" value="Please log in first" />
                </jsp:forward>
            </c:if>



            <div id="main" class="round" >
                <p class="titleBlock"> Upload Capabilities </p>
                <div class="messageError">
                    <p> ${messageError} </p>
                </div>
                <form method="post" action="UploadData"  enctype="multipart/form-data" class="formUser"  >
                    <ul>
                        <li> <label> Name </label> <input type="text"  id="nameCap" name="nameCap" /> </li>
                        <li> <label> Description </label> <textarea cols="40" rows="5" id="comments" name="comments"  > </textarea> </li>
                        <li> <label> Tags </label> <input type="text" id="tags" name="tags" /> </li>
                        <li> <label> Select a file to upload (Max size: 10MB)</label> <input type="file" id="uploadfile" name="uploadfile"  /> </li>
                        <li> <input type="hidden" name="todo" value="upload" /> </li>
                        <li> <input type="submit" value="Upload" /> </li>
                    </ul>

                    <script type="text/javascript">
                       $(document).ready(function(){
                        $("#tags").tokenInput("AutoComplete", {theme: "facebook"});
                       });
                        

                    </script>

                </form>
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
