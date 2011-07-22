/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userdata;

import java.sql.*;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;



/**
 *
 * @author nachomv
 */
public class Database extends HttpServlet{

    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

  @Override
  public void init(ServletConfig conf) throws ServletException{
      super.init(conf);
      String urlJDBC = conf.getInitParameter("urlJDBC");
      String driverJDBC = conf.getInitParameter("driverJDBC");
      String userJDBC = conf.getInitParameter("userJDBC");
      String passwordJDBC = conf.getInitParameter("passwordJDBC");

      try{
          Class.forName("com.mysql.jdbc.Driver");
          String urlPath = getServletContext().getRealPath(urlJDBC);
          if(connection == null ){
          //    connection = DriverManager.getConnection("jdbc:msysql:" + urlPath, userJDBC, passwordJDBC );
              connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gsiWeb", "root", "root");
      }
      }
      catch(SQLException exSQL ){
         LOGGER.severe("Error creating the connection " + exSQL.getMessage());
         throw new ServletException( exSQL.getMessage() );
      }
      catch(ClassNotFoundException exClass){
          LOGGER.severe("Error loading the Driver " + exClass.getMessage());
          throw new ServletException(exClass.getMessage());
      }
}

@Override
public void destroy(){
  try{
      connection.close();
  }
  catch(SQLException exSQL){
      LOGGER.severe("Error closing the connection " + exSQL.getMessage());
      
  }
}


    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
        if (action == null) {
            LOGGER.warning("Action null");
            return;
        }
        if (action.equals("saveData")) {
            PreparedStatement smt;
            LOGGER.severe("Action: save data");
            
            try {
                
                HttpSession session = request.getSession(true);
                Capabilities cap = (Capabilities) session.getAttribute("capabilitie");
                
                //Capabilities cap = new Capabilities( "testCap", "0", actualDate, "testUser", "testComments" );

                String name = cap.getName();
                LOGGER.severe(name);
                String id = cap.getId();
                Long date = cap.getDate();
                String userUpload = cap.getUserUpload();
                String comments = cap.getComments();
                
                smt = connection.prepareStatement("INSERT INTO capabilities (USER,ID,DATE,USERUPLOAD, COMMENTS) VALUES(?,?,?,?,?)");
                smt.setString(1, name);
                smt.setLong(2,date);
                smt.setString(3, id);
                smt.setString(4,userUpload);
                smt.setString(5, comments);
                smt.executeUpdate();
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("uploadCorrect.jsp");
                dispatcher.forward(request, response);
            } catch (SQLException ex) {
                LOGGER.info("Fallo Insert " + ex.getMessage());
                throw new ServletException("SQL Insert " + ex.getMessage());
            }


}
}




}