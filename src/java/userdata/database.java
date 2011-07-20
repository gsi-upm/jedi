
package userdata;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

/**
 * Modificado el 7 de julio.
 * Falta crear la base de datos, añadir usuarios a ella a traves del formulario.
 * @author nachomv
 */
public class database extends HttpServlet{

    private Connection con;
    
    /**
     * Inicializamos el servlet con JDBC
     * @param conf objeto de configuración del servlet. Presupone en este ejemplo
     * que el parámetro urlJDBC contiene la URL de la conexion y driverJDBC la
     * clase del driver
     * @throws javax.servlet.ServletException si hay algún fallo
     */
    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        // String databse = conf.getInitParameter("hsqldb.server.database");
        String urlJDBC = conf.getInitParameter("urlJDBC");
        String driverJDBC = conf.getInitParameter("driverJDBC");
        String userJDBC = conf.getInitParameter("userJDBC");
        String passwordJDBC = conf.getInitParameter("passwordJDBC");


        try {
            Class.forName("org.hsqldb.jdbcDriver");
            // con = DriverManager.getConnection(urlJDBC, userJDBC, passwordJDBC);
            //// Algo del tipo /WEB-INF/db/escuela
            String urlPath = getServletContext().getRealPath(urlJDBC);

            if (con == null) {
                con = DriverManager.getConnection("jdbc:hsqldb:file:" + urlPath, userJDBC, passwordJDBC);
            }
        } catch (SQLException exSQL) {
            throw new ServletException("init conexión " + exSQL.getMessage());
        } catch (ClassNotFoundException exClass) {
            throw new ServletException("init clase del driver " + exClass.getMessage());
        }

    }

    @Override
    public void destroy() {
        try {
            con.close();
        } catch (SQLException ex) {
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("accion");
        if (action == null) {
            // ver qué hacer, procesar mensaje, etc.
            return;
        }
        if (action.equals("newuser")) {
            String user = request.getParameter("user");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            PreparedStatement smt;
            try {
                smt = con.prepareStatement("INSERT INTO USUARIOS (user,email,password) VALUES(?,?,?)");
                smt.setString(1, user);
                smt.setString(2, email);
                smt.setString(3, password);
                smt.executeUpdate();
                RequestDispatcher dispatcher = request.getRequestDispatcher("registersuccesful.jsp");
                dispatcher.forward(request, response);
            } catch (SQLException ex) {
                throw new ServletException("SQL Insert " + ex.getMessage());
            }

        } 
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}




