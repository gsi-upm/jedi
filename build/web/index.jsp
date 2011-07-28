<%--
Document   : index
Created on : 02-jul-2011, 15:40:49
Author     : nachomv
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="URL_PRINCIPAL" scope="session" value="index.jsp"/>
<c:url var="SAVE_DATA" scope="session" value="Database" >
    <c:param name="action" value="saveData" />
</c:url>
<c:url var="SHOW_DATA" scope="session" value="Database">
    <c:param name="action" value="showData" />
</c:url>

<c:url var="DOWNLOAD_DATA" scope="session" value="DownloadData" />



<html>
    <div class="container">
        <head>
            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>
            <div id="main" class="round">
                <p> Repositorio de agentes de JADEX </p>
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>
