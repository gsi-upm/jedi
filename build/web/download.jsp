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
    <style type="text/css" title="currentStyle">
        @import "css/dataTables/demo_page.css";
        @import "css/dataTables/demo_table.css";
    </style>


    <!--<%@include file="/WEB-INF/jspf/tagCloud.jspf" %>-->
       <c:if test="${validUser == null}" >
                <jsp:forward page="login.jsp">
                    <jsp:param name="origUrL" value="download.jsp" />
                    <jsp:param name="messageError" value="Please log in first" />
                </jsp:forward>
            </c:if>



    <script type="text/javascript" src="js/jquery-1.4.4.min.js"> </script>    
    <script type="text/javascript" src="js/jquery.dataTables.js"> </script>
    <script type="text/javascript" charset="utf-8">
     var oTable;

        /* Formating function for row details */
        function fnFormatDetails ( nTr )
        {
            var aData = oTable.fnGetData( nTr );
            var sOut = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
            sOut += '<tr><td>Rendering engine:</td><td>'+aData[2]+' '+aData[5]+'</td></tr>';
            sOut += '<tr><td>Link to source:</td><td>Could provide a link here</td></tr>';
            sOut += '<tr><td>Extra info:</td><td>And any further details here (images etc)</td></tr>';
            sOut += '</table>';

            return sOut;
        }




        $(document).ready(function() {
         oTable =   $('#displayData').dataTable( {
                "bProcessing": true,
                "bStateSave": true,
                "bServerSide": true,
                "sAjaxSource": "Database?action=showData",
                "aoColumns": [
                    { "sName": "Name", "sTitle": "Name", "sWidth": "20%", "bSortable": "true" },
                    { "sName": "DateUpload", "sTitle": "Upload Date", "sWidth": "20%", "bSortable": "true" },
                    { "sName": "user", "sTitle": "Uploaded by", "sWidth": "20%", "bSortable": "true" },
                    { "sName": "tags", "sTitle": "Tags", "sWidth": "20%", "bSortable": "false"}
                    

                ],
                "sPaginationType": "full_numbers",
                "aaSorting": [[1,'asc']],
                "oLanguage": {
                    "sLengthMenu": "Page length: _MENU_",
                    "sSearch": "Filter:",
                    "sZeroRecords": "No matching records found"
                },

                "fnServerData": function ( sSource, aoData, fnCallback ) {

                    aoData.push(
                    { "name": "table", "value": "uklocationcodes" },
                    { "name": "sql", "value": "SELECT id, varCode, varLocation" }
                );

                    $.ajax( {"dataType": 'json',
                        "type": "POST",
                        "url": sSource,
                        "data": aoData,
                        "success": fnCallback} );
                }
            } );
        });

        $('#displayData tbody td img').live( 'click', function () {
            var nTr = this.parentNode.parentNode;
            if ( this.src.match('details_close') )
            {
                /* This row is already open - close it */
                this.src = "images/details_open.png";
                oTable.fnClose( nTr );
            }
            else
            {
                /* Open this row */
                this.src = "images/details_close.png";
                oTable.fnOpen( nTr, fnFormatDetails(nTr), 'details' );
            }
        } );


    </script>

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
                                <li> ${capsCounter.count} - ${capsRating.name} </li>
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
         
            <div id="main" class="round">
                <div class="messageError">
                    <p>    ${fn:escapeXml(messageError)}</p>
                </div>
                <div id="capabilities">
                    <div id="dt_displayData">
                        <div id="container">
                    <table cellpadding="0" cellspacing="0" border="0" class="display" id="displayData">
                        <thead>
                            <tr class="gradeA">
                                <th align="left">Name</th>
                                <th align="left">Upload Date</th>
                                <th align="left">Upload User</th>
                                <th align="left">Tags</th>
                                
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td colspan="3" class="dataTables_empty">Loading data from server</td>
                            </tr>
                        </tbody>
                    </table>
                            <div class="spacer"> </div>
                    </div>
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