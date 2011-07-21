/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userdata;

import java.sql.*;
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
              connection = DriverManager.getConnection("jdbc:msysql:file" + urlPath, userJDBC, passwordJDBC );
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


protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("accion");
        if (action == null) {
            // ver qué hacer, procesar mensaje, etc.
            LOGGER.warning("Recibido Action null");
            return;
        }
        if (action.equals("addStudent")) {
            PreparedStatement smt;
            try {
                smt = connection.prepareStatement("INSERT INTO ALUMNOS (DNI,NOM,NDIR,CURSO) VALUES(?,?,?,?)");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/ejemploConServlets/menu.jsp");
                dispatcher.forward(request, response);
            } catch (SQLException ex) {
                LOGGER.info("Fallo Insert " + ex.getMessage());
                throw new ServletException("SQL Insert " + ex.getMessage());
            }


}
}




}