<%-- 
    Document   : download
    Created on : Jul 24, 2011, 12:02:56 PM
    Author     : nachomv
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



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
                <div id="capabilities">
                    <div class="capName">
                        <c:choose>
                            <c:when test="${resCapabilities.rowCount eq 0}">
                                <select name="capList" size="4">
                                    No capabilities
                                </select>
                            </c:when>
                            <c:otherwise>
                                <form name="formCap" method="post" action="#" class="formUser">
                                     
                                      <div id="selectOption">
                                          <p> Capabilities </p>
                                       <select name="capListName" multiple class="selectForm" size="4" >
                                        <c:forEach var="capability" items="${resCapabilities.rows}">
                                            <option> ${capability.name} </option>
                                        </c:forEach>
                                    </select>
                                          <p> Download </p>
                                    <select name="capsSelected" multiple size="4" class="selectForm">
                                      
                                    </select>
                                      </div>
                                      <input type="button" value="Add to list" onClick="addSelect( formCap.capListName.options[formCap.capListName.selectedIndex].value )" />
                                      <input type="submit" value="Download" />
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>


        <script language="javascript" type="text/javascript">
            function addSelect(  text ){
                var option1 = new Option(text,"textOption","","");
                var select = document.formCap.capsSelected;
                select.appendChild(option1);
            }
               
        </script>
</html>
