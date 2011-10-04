package modules;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.hsqldb.Database;

import com.google.gson.*;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Autocomplete: Gets info from database to use it to autocomplete forms
 * @author nachomv
 *
 */
public class AutoComplete extends HttpServlet {

    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LOGGER.info("IM HERE!!!");
            String query = request.getParameter("q");
            AutoComplete auto = new AutoComplete();
            //String query = request.getParameter("q");

            /*Iterator<String> iterator = countries.iterator();
            while(iterator.hasNext()) {
            String country = (String)iterator.next();
            out.println(country);
            }*/
            auto.getData(query, response);


        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    private int totalTags;
    private List<String> tags;
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    /**
     * It get's connected to the database to look for tags
     * @throws SQLException if connection fails
     * @throws ClassNotFoundException if Driver Class is not found
     */
    public AutoComplete() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jediweb", "jediweb", "LM8h36FCeTCD9vxh");
            }
        } catch (SQLException exSQL) {
            LOGGER.severe("Error creating the connection " + exSQL.getMessage());
        } catch (ClassNotFoundException exClass) {
            LOGGER.severe("Error loading the Driver " + exClass.getMessage());
        }

        tags = new ArrayList<String>();
        PreparedStatement sm = connection.prepareStatement("SELECT * from capabilities");
        ResultSet resultSet = sm.executeQuery();

        while (resultSet.next()) {
            String keyTemp = resultSet.getString("keywords");
            StringTokenizer st = new StringTokenizer(keyTemp, ";");
            while (st.hasMoreTokens()) {
                tags.add(st.nextToken().trim());
            }

        }
        totalTags = tags.size();
        LOGGER.info("TOTAL tags: " + String.valueOf(totalTags));
    }

    /**
     * getData: Return match words
     * @param query: String wanted to look for matches
     * @return: List with string matched
     */
    public String getData(String query, HttpServletResponse response) throws java.io.IOException {
        try {



            query = query.toLowerCase();

            List<Suggestions> suggestions = new ArrayList<Suggestions>();

            for (int i = 0; i < totalTags; i++) {
                String tagTemp = tags.get(i).toLowerCase();
                if (tagTemp.startsWith(query)) {
                    Suggestions temp = new Suggestions();
                    temp.setId(i);
                    temp.setName(tagTemp);
                    suggestions.add(temp);
                    

                }
            }

            Gson gson = new Gson();
            String jsonOutput = gson.toJson(suggestions);
            LOGGER.info("JSON: " + jsonOutput);
            //return matched;
            //return jsonOutput;


            //response.getOutputStream().write());



            ServletOutputStream sos = response.getOutputStream();


            response.getOutputStream().print(gson.toJson(suggestions));

            return jsonOutput;


        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());
            return null;
        }
    }
}
