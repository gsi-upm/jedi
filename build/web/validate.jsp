<%-- 
    Document   : validate
    Created on : 04-jul-2011, 13:40:18
    Author     : imendizabal
--%>

 <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

  
  <%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
  <%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
  <%@page language="java" import="userdata.User, userdata.Error" %>

  
    <html>
    <div class="container">
    <head>
       <%@include file="/WEB-INF/jspf/header.jspf" %>
        <link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
    </head>

      <body>
        <div id="main" class="round">
            <div id="validate" class=".formUser">

                <jsp:useBean id="error" class="userdata.Error" scope="request" />
                <jsp:useBean id="data" class="userdata.User" scope="request" />

                <c:if test="${empty param.user}" var="emptyUser">
                    <jsp:setProperty name="error" property="messageError" value="Please, enter an username" />
                </c:if>

                <c:if test="${!emptyUser}">
                    <sql:query var="userCheck">
                        select * from dataUsers
                        where user = ?
                        <sql:param value="${param.user}"/>
                    </sql:query>
                        <c:choose>
                            <c:when test="${userCheck.rowCount != 0}">
                                <jsp:setProperty name="error" property="messageError" value="The user name is already registered" />
                            </c:when>
                        </c:choose>
                </c:if>
                
                <c:if test="${empty param.email}" var="emptyEmail" >
                    <jsp:setProperty name="error" property="messageError" value="Please enter an email" />
                </c:if>

                 <!-- Email validation -->
                 <c:if test="${!emptyEmail}">
                    <c:set var="existsAt" value="${fn:contains(param.email, '@')}" />
                    <c:set var="existsDos" value="${fn:contains(param.email, '.')}" />
                    <c:set var="beforeAt" value="${fn:substringBefore(param.email, '@')}" />
                    <c:set var="afterAt" value="${fn:substringAfter(param.email, '@')}" />
                    <c:set var="sizeBefore" value="${fn:length(beforeAt)}" />
                    <c:set var="sizeAfter" value="${fn:length(afterAt)}" />
                    <c:set var="domainSufix" value="${fn:substringAfter(param.email, '.')}" />
                    <c:if test="${(!existsAt) || (!existsDos) || (sizeBefore == 0) || (sizeAfter) == 0 || (domainSufix == 0) }">
                        <jsp:setProperty name="error" property="messageError" value="Email incorrect" />
                    </c:if>

                    <sql:query var="emailCheck">
                        select * from dataUsers
                        where email = ?
                        <sql:param value="${param.email}"/>
                    </sql:query>
                        <c:choose>
                            <c:when test="${emailCheck.rowCount != 0}">
                                <jsp:setProperty name="error" property="messageError" value="The email address is already registered" />
                            </c:when>
                        </c:choose>
                        
                </c:if>


                <!-- Password validation -->
                <c:if test="${empty param.password}" var="emptyPassword">
                    <jsp:setProperty name="error" property="messageError" value="Please enter your password" />
                </c:if>
                <c:if test="${!emptyPassword}">
                    <c:set var="passLength" value="${fn:length(param.password)}" />
                    <c:if test="${ passLength < 6 || passLength > 30}">
                          <jsp:setProperty name="error" property="messageError" value="Password must have between 6 and 30 characters" />
                    </c:if>                    
                </c:if>


                <c:if test="${empty param.repeatPassword}">
                    <jsp:setProperty name="error" property="messageError" value="Please enter your password again" />
                </c:if>

                <
                <c:if test="${param.password != param.repeatPassword}">
                    <jsp:setProperty name="error" property="messageError" value="Passwords don't match" />
                </c:if>

               <c:if test="${empty error.messageError}" var="errorForm">
                   <jsp:useBean id="correctdata" class="userdata.User" scope="request" />
                   <jsp:setProperty name="correctdata" property="*" />
                   <jsp:forward page="registersuccesful.jsp" />
               </c:if>
         
               <c:if test="${!errorForm}">
                    <jsp:forward page="register.jsp" />
                </c:if>
       

                
            </div>
        </div>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    </div>
    </html>
