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
import java.util.logging.Logger;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import java.util.Random;


/**
 *
 * @author nachomv
 */
public class Database extends HttpServlet {

    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static final String DESTINATION_DIR_PATH = "/files";

    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        String urlJDBC = conf.getInitParameter("urlJDBC");
        String driverJDBC = conf.getInitParameter("driverJDBC");
        String userJDBC = conf.getInitParameter("userJDBC");
        String passwordJDBC = conf.getInitParameter("passwordJDBC");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String urlPath = getServletContext().getRealPath(urlJDBC);
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gsiWeb", "root", "root");
            }
        } catch (SQLException exSQL) {
            LOGGER.severe("Error creating the connection " + exSQL.getMessage());
            throw new ServletException(exSQL.getMessage());
        } catch (ClassNotFoundException exClass) {
            LOGGER.severe("Error loading the Driver " + exClass.getMessage());
            throw new ServletException(exClass.getMessage());
        }
    }

    @Override
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException exSQL) {
            LOGGER.severe("Error closing the connection " + exSQL.getMessage());

        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        petitionAux(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      petitionAux( request, response );
    }

    private void petitionAux(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
          try {
            String action = request.getParameter("action");
            if (action == null) {
                LOGGER.warning("Action null");
                return;
            }
            if (action.equals("saveData")) {
                PreparedStatement smt;
                LOGGER.severe("Action: save data");
                HttpSession session = request.getSession(true);
                Capabilities cap = (Capabilities) session.getAttribute("capabilitie");

                String name = cap.getName();
                java.sql.Date date = cap.getDate();
                String timeUpload = cap.getTimeUpload();
                String userUpload = cap.getUserUpload();
                String comments = cap.getComments();
                String id = id();
                String nameFolder = cap.getNameFile();

                //Check if the capability already exists
                //Statement sm = connection.createStatement();
               // ResultSet resultSet = sm.executeQuery("SELECT * FROM capabilities where name ="+name);
                //LOGGER.info("There is already a capability with this name, it will be saved with a new one");

                String javaFiles = "";
                for(int i=0;i<cap.getListFile().size();i++){
                    javaFiles = javaFiles +  cap.getListFile().get(i).getName() + ';';
                    
                }

                smt = connection.prepareStatement("INSERT INTO capabilities (NAME,DATEUPLOAD,TIMEUPLOAD, ID,USERUPLOAD, COMMENTS, JAVAFILES, NAMEFOLDER) VALUES(?,?,?,?,?,?,?,?)");
                smt.setString(1, name);
                smt.setDate(2, date);
                smt.setString(3, timeUpload);
                smt.setString(4, id);
                smt.setString(5, userUpload);
                smt.setString(6, comments);
                smt.setString(7, javaFiles);
                smt.setString(8, nameFolder);
                smt.executeUpdate();
                LOGGER.severe("Data saved");

                RequestDispatcher dispatcher = request.getRequestDispatcher("uploadCorrect.jsp");
                dispatcher.forward(request, response);
            } else if (action.equals("showData")) {
                Statement smt = connection.createStatement();
                ResultSet resultSet = smt.executeQuery("SELECT * FROM capabilities");
                Result result = ResultSupport.toResult(resultSet);
                request.setAttribute("resCapabilities", result);
                RequestDispatcher dispatcher = request.getRequestDispatcher("download.jsp");
                dispatcher.forward(request, response);
            }
            else if(action.equals("emptyFile")){
                request.getSession().setAttribute("messageError", "Please, write a name and select a file");
                RequestDispatcher dispatcher = request.getRequestDispatcher("upload.jsp");
                dispatcher.forward(request, response);
            }

            else if(action.equals("deleteUser")){
                PreparedStatement smt;
                HttpSession session = request.getSession(true);
                User user = (User) session.getValue("validUser");
                String nameUser = user.getUser();
                String emailUser = user.getEmail();
                String statement = "DELETE FROM dataUsers where user = '" + nameUser + "' AND email = '" + emailUser + "'";
                LOGGER.info("Consulta SQL: " + statement);
                smt = connection.prepareStatement(statement);
                smt.executeUpdate();
                LOGGER.info("User name:" + nameUser);
                LOGGER.info("Email user: " + emailUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("deleteUser.jsp");
                dispatcher.forward(request, response);
                
            }
            
        } catch (SQLException ex) {
            LOGGER.info("Fallo Insert " + ex.getMessage());
            throw new ServletException("SQL Insert " + ex.getMessage());
        } 
        
    }

    /**
     * Get a non-repeated id for each capabilitiy
     * @return
     * @throws java.sql.SQLException
     */
    private String id() throws java.sql.SQLException {
        try {
            //Random ID for each capabilitie uploaded
            Random r = new Random();
            double tempInt = r.nextDouble();
            tempInt = tempInt * 100000;
            tempInt = Math.round(tempInt);
            String id = String.valueOf((int) tempInt);

            Statement sm = connection.createStatement();
            ResultSet resultSet = sm.executeQuery("SELECT * FROM capabilities WHERE ID =" + id);
            resultSet.first();
            int col = resultSet.getRow();
            if (col != 0) {
                id();
            }
            return id;
        } catch (java.sql.SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "";
        }

    }
}
