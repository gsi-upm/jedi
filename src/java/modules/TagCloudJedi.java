

package modules;

import org.mcavallo.opencloud.*;



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

/**
 *
 * @author nachomv
 */
public class TagCloudJedi extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TagCloudJedi.class.getName());

    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);    
    }

      @Override
    public void destroy() {
      }

      @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //   petitionAux(request, response);
    }

      @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //   petitionAux(request, response);
    }

      private void petitionAux(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          Cloud cloud = new Cloud();
          cloud.setMaxWeight(35.0);
          cloud.addTag(new Tag("Google"));
          cloud.addTag(new Tag("ElMundo"));
          cloud.addTag(new Tag("ABC"));
          cloud.addTag(new Tag("Mac"));
          cloud.addTag(new Tag("PC"));

          RequestDispatcher disp = request.getRequestDispatcher("main.jsp");
        //  request.getSession().setAttribute("messageError", "There is already a capability whith this name, please write a new one");
          disp.forward(request, response);

          
      }


}
