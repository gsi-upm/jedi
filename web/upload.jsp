<%-- 
    Document   : upload
    Created on : 04-jul-2011, 11:21:30
    Author     : imendizabal
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" import="javazoom.upload.*" %>
<%@page language="java" import="java.util.*" %>
<%@page errorPage="error.jsp" %>


<%
    String direccion = request.getSession().getServletContext().getRealPath("imagenesDB/");
%>

<jsp:useBean id="upBean" scope="page" class="javazoom.upload.UploadBean" >
    <jsp:setProperty name="upBean" property="folderstore" value="<%= direccion%>" />
    <jsp:setProperty name="upBean" property="overwritepolicy" value="nametimestamp"/>
</jsp:useBean>


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
            <form method="post" action="upload.jsp" name="upload" enctype="multipart/form-data" class="formUser" > 
                <ul>
                    <li> <label> Name </label> <input type="text" name="Name" /> </li>
                    <li> <label> Comments </label> <textarea cols="40" rows="5" name="comments"> </textarea> </li>
                    <li> <label> Select a file to upload </label> <input type="text" name="uploadfile" /> </li>
                    <li> <input type="hidden" name="todo" value="upload" /> </li
                    <li> <input type="submit" value="Upload" /> </li>
                </ul>
            </form>

<%
if (MultipartFormDataRequest.isMultipartFormData(request)) {
 MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
 String todo = null;
 if (mrequest != null) {
  todo = mrequest.getParameter("todo");
 }
 if ((todo != null) && (todo.equalsIgnoreCase("upload"))) {
  Hashtable files = mrequest.getFiles();
  if ((files != null) && (!files.isEmpty())) {
   java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyMMddHHmmss");
   String archivo = ((UploadFile) mrequest.getFiles().get("uploadfile")).getFileName();
   int posicionPunto = archivo.indexOf(".");
   String nombreImagen = archivo.substring(0, posicionPunto);
   String extension = archivo.substring(posicionPunto);
   nombreImagen = nombreImagen + formato.format(new java.util.Date());
   nombreImagen = nombreImagen + extension;
   ((UploadFile) mrequest.getFiles().get("uploadfile")).setFileName(nombreImagen);
   UploadFile file = (UploadFile) files.get("uploadfile");
   if (file != null) {
    out.println("El archivo: " + file.getFileName() + " se subio correctamente");
   }
   upBean.store(mrequest, "uploadfile");
  } else {
    out.println("Archivos no subidos");
  }
 } else {
   out.println("<BR> todo=" + todo);
 }
}
%>

</div>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    </div>
</html>
