/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userdata;

import java.sql.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        petitionAux(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        petitionAux(request, response);
    }

    private void petitionAux(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                PreparedStatement sm = connection.prepareStatement("SELECT * FROM capabilities where name=?");
                sm.setString(1, name);
                ResultSet resultSet = sm.executeQuery();


                if (resultSet.first() == true) {

                    RequestDispatcher disp = request.getRequestDispatcher("upload.jsp");
                    request.getSession().setAttribute("messageError", "There is already a capability whith this name, please write a new one");
                    disp.forward(request, response);
                } else {


                    String javaFiles = "";
                    for (int i = 0; i < cap.getListFile().size(); i++) {
                        javaFiles = javaFiles + cap.getListFile().get(i).getName() + ';';

                    }

                    smt = connection.prepareStatement("INSERT INTO capabilities (NAME,DATEUPLOAD,TIMEUPLOAD, ID,USERUPLOAD, COMMENTS, JAVAFILES, NAMEFOLDER, TIMESDOWNLOADED) VALUES(?,?,?,?,?,?,?,?,?)");
                    smt.setString(1, name);
                    smt.setDate(2, date);
                    smt.setString(3, timeUpload);
                    smt.setString(4, id);
                    smt.setString(5, userUpload);
                    smt.setString(6, comments);
                    smt.setString(7, javaFiles);
                    smt.setString(8, nameFolder);
                    smt.setInt(9, 0);
                    smt.executeUpdate();
                    LOGGER.severe("Data saved");

                    RequestDispatcher dispatcher = request.getRequestDispatcher("uploadCorrect.jsp");
                    dispatcher.forward(request, response);
                }
            } else if (action.equals("showData")) {
                Statement smt = connection.createStatement();
                ResultSet resultSet = smt.executeQuery("SELECT * FROM capabilities");
                Result result = ResultSupport.toResult(resultSet);
                request.setAttribute("resCapabilities", result);


                //Looks for a list with the top downloaded capabilities

                String count = "SELECT COUNT(*) from capabilities";
                ResultSet resCount = smt.executeQuery(count);
                Integer tableLength = 0;
                while (resCount.next()) {
                    tableLength = resCount.getInt(1);
                }

                if (tableLength != 0) {
                    String query = "SELECT name, timesdownloaded from capabilities order by timesdownloaded desc";
                    ResultSet resultCaps = smt.executeQuery(query);
                    Result resultCap = ResultSupport.toResult(resultCaps);
                    request.setAttribute("resCapsOrdered", resultCap);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("download.jsp");
                dispatcher.forward(request, response);

            } else if (action.equals("emptyFile")) {
                request.getSession().setAttribute("messageError", "Please, write a name and select a file");
                RequestDispatcher dispatcher = request.getRequestDispatcher("upload.jsp");
                dispatcher.forward(request, response);
            } else if (action.equals("deleteUser")) {
                PreparedStatement smt;
                HttpSession session = request.getSession(true);
                User user = (User) session.getValue("validUser");
                String nameUser = user.getUser();
                String emailUser = user.getEmail();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM dataUsers where user=? AND email=?");
                statement.setString(1, nameUser);
                statement.setString(2, emailUser);

                statement.executeUpdate();
                LOGGER.info("User name:" + nameUser);
                LOGGER.info("Email user: " + emailUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("deleteUser.jsp");
                dispatcher.forward(request, response);
            } else if (action.equals("forgotPass")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("forgot.jsp");
                String email = request.getParameter("email");
                String userName = request.getParameter("username");
                if (email == null && userName == null) {
                    request.getSession().setAttribute("messageError", "Please, write your username or your email");
                    dispatcher.forward(request, response);
                }
                //Email validation
                Pattern pat = null;
                Matcher mat = null;
                pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
                mat = pat.matcher(email);
                if (!mat.find()) {
                    request.getSession().setAttribute("messageError", "Please, write your email correctly");
                }
                PreparedStatement smt;
                //New password
                Random r = new Random();
                double tempInt = r.nextDouble();
                tempInt = tempInt * 100000000;
                tempInt = Math.round(tempInt);
                String newPass = String.valueOf((int) tempInt);


                PreparedStatement statementTwo;
                if (email != null) {
                    String updatePass = "update gsiWeb.dataUsers " + "set password = ? where email = ?";
                    statementTwo = connection.prepareStatement(updatePass);
                    statementTwo.setString(1, newPass);
                    statementTwo.setString(2, email);
                    request.getSession().setAttribute("newPassword", "Your new password is: " + newPass);
                    dispatcher.forward(request, response);


                } else if (userName != null) {
                    String updatePass = "update gsiWeb.dataUsers " + "set password = ? where user = ?";
                    statementTwo = connection.prepareStatement(updatePass);
                    statementTwo.setString(1, newPass);
                    statementTwo.setString(2, userName);
                    request.getSession().setAttribute("newPassword", "Your new password is: " + newPass);
                    dispatcher.forward(request, response);
                }





            }


        } catch (SQLException ex) {
            LOGGER.info("Error Insert " + ex.getMessage());
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

            PreparedStatement sm = connection.prepareStatement("SELECT * FROM capabilities WHERE ID=?");
            sm.setString(1, id);
            ResultSet resultSet = sm.executeQuery();
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
