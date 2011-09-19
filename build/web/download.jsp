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
    <%@include file="/WEB-INF/jspf/tagCloud.jspf" %>

    <div id="topRatingCaps" class="formUser">
        <c:choose>
            <c:when test="${resCapsOrdered.rowCount eq 0}">
                No capabilities to show
            </c:when>
            <c:otherwise>
                <p> Capabilities downloaded </p>
                <ul class="round">

                    <c:forEach var="capsRating" items="${resCapsOrdered.rows}" varStatus="capsCounter">
                        <c:choose>
                            <c:when test="${capsCounter.count <= 5}">

                                <li> ${capsRating.name} </li>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="container">
        <head>




            <%@include file="/WEB-INF/jspf/header.jspf" %>
            <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
        </head>

        <body>




            <c:if test="${validUser == null}" >
                <jsp:forward page="login.jsp">
                    <jsp:param name="origUrL" value="${pageContext.request.requestURL}" />
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
                                <form name="formCap" method="post" action="DownloadData" class="formUser">
                                    <div id="selectOption">
                                        <p> Capabilities </p>
                                       <!-- <select name="capListName" multiple class="selectForm" size="4" >-->
                                       <ul class="selectForm">
                                           <c:forEach var="capability" items="${resCapabilities.rows}" varStatus="selectCounter">
                                                <!--<option ondblclick="addSelect( formCap.capListName.options[formCap.capListName.selectedIndex].value )"> ${capability.name}  </option>-->

                                                
                                            </c:forEach>
                                       </ul>
                                        <!--</select>-->
                                        <p> Download </p>
                                        <select name="capsSelected" multiple size="4" class="selectForm">
                                        </select>
                                        <input type="hidden" name="listParameters" id="listParameters" value="empty"/>
                                    </div>
                                    <input type="button" value="Add" onClick="addSelect( formCap.capListName.options[formCap.capListName.selectedIndex].value )" />
                                    <input type="button" value="Delete" onClick="deleteSelext( formCap.capsSelected.selectedIndex)" />
                                    <input type="submit" value="Download" />
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
    </div>






</body>
<%@include file="/WEB-INF/jspf/footer.jspf" %>

<script language="javascript" type="text/javascript">
    function addSelect( text ){
        var option1 = new Option(text,"textOption","","");
        var select = document.formCap.capsSelected;

        var lengthCapsSelect = document.formCap.capsSelected.length;
        var alreadyInTheList = false;
        for(i=0;i<lengthCapsSelect;i++){

            if( document.formCap.capsSelected.options[i].text == text ){
                alreadyInTheList = true;
            }
        }
        if( alreadyInTheList == false ){
            select.appendChild(option1);
            document.getElementById("listParameters").value += text;
            document.getElementById("listParameters").value += ',';
        }
    }

    function deleteSelext( selected){
        var mySelect = document.forms['formCap'].elements['capsSelected'];
        mySelect.options[selected] = null;
    }

      

</script>


</html>