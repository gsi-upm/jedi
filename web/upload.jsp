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
    <!--<%@include file="/WEB-INF/jspf/tagCloud.jspf" %>-->
    <script type="text/javascript" src="js/jquery.min.js"> </script>
    <script type="text/javascript" src="js/jquery.tokeninput.js"> </script>
    <!-- Add autocomplete function to tags form-->
    <script type="text/javascript">
        $(document).ready(function(){
            $("#tags").tokenInput("AutoComplete", {theme: "facebook"});
        });
    </script>




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
                        <div id="tagsText">
                            <li> <label> Tags </label> <input type="text" id="tags" name="tags" /> </li>
                        </div>
                        <div id="newTag">
                            <li> <label> Add a new tag </label> <input type="text" name="addTag" id="addTag" /> </li>
                            <input type="button" onclick="addNewTag()" value="Add a new tag" />
                        </div>

                        <li> <label> Select a file to upload (Max size: 10MB)</label> <input type="file" id="uploadfile" name="uploadfile"  /> </li>
                        <input type="hidden" name="listTags" id="listTags" />
                        <li> <input type="submit" value="Upload" onclick="buildTags()"/> </li>
                    </ul>


                </form>
            </div>
            <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </body>
    </div>
</html>



<script type="text/javascript">
    /*  buildTags:
     *   It makes a list with all tags clicked and put them together in a string
     */
    function addNewTag(){
        var nodeTags = document.getElementById("tagsText").getElementsByTagName("ul");
        var ulTags = nodeTags[0];
        var li = document.createElement("li");
        li.setAttribute("class", "token-input-token-facebook");
        
        var span = document.createElement("span")
        span.setAttribute("class", "token-input-delete-token-facebook");
        var x = document.createTextNode("x");
        span.appendChild(x);
        
        var p = document.createElement("p");
        var newTag = document.getElementById("addTag");
        var textTag = document.createTextNode( newTag.value );
        p.appendChild( textTag );
        li.appendChild( p );
        li.appendChild( span );
        
        ulTags.appendChild(li);
        

    }

    function buildTags(){
        tags = "";
        var nodeTags = document.getElementById("tagsText").getElementsByTagName('p');
        if( nodeTags == null ){
            document.getElementById("listTags").value += "";
            return tags;
        }
        else{
            for( i = 0; i < nodeTags.length; i++ ){
                tags = tags + nodeTags[i].textContent + ';';
            }
            document.getElementById("listTags").value += tags;
        }
        return tags;
    }
</script>