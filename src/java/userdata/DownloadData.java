/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userdata;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

/**
 *
 * @author nachomv
 */
public class DownloadData extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        helpDoPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        helpDoPost(request, response);

    }

    private void helpDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String [] listCap = request.getParameterValues("capListName");
        for(int i=0;i<listCap.length;i++){
            LOGGER.severe("Tamano: " + listCap.length);
            LOGGER.severe("Lista: " + listCap[i]);
        }
        
    }
}
