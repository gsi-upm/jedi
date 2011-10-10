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
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;

import java.util.logging.Logger;
import org.apache.commons.compress.compressors.gzip.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.io.IOUtils;

import javax.servlet.RequestDispatcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import javax.servlet.ServletOutputStream;
import java.sql.*;

/**
 *
 * @author nachomv
 */
public class DownloadData extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private Connection connection;

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
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jediweb", "jediweb", "LM8h36FCeTCD9vxh");

            }
        } catch (SQLException exSQL) {
            LOGGER.severe("Error creating the connection " + exSQL.getMessage());
            //throw new ServletException(exSQL.getMessage());
        } catch (ClassNotFoundException exClass) {
            LOGGER.severe("Error loading the Driver " + exClass.getMessage());
            //throw new ServletException(exClass.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            helpDoPost(request, response);
        }
        catch( Exception ex ){
            LOGGER.info("Exception ex: " + ex.getMessage());
            handleError(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            helpDoPost(request, response);
        }
        catch( Exception ex ){
            LOGGER.info("Exception ex: " + ex.getMessage());
            handleError(request, response);
        }

    }

    /**
     * Check if exist necessary folders to compress and download files
     */
    private void checkFolders() {
        String fileTemp = getServletContext().getRealPath("/files") + "/temp";
        File fileT = new File(fileTemp);
        if (!fileT.isDirectory()) {
            fileT.mkdir();
            LOGGER.info("Directory " + fileTemp + " created.");
        }
        String fileCompress = fileTemp + "/compress";
        File fileComp = new File(fileCompress);
        if (!fileComp.isDirectory()) {
            fileComp.mkdir();
            LOGGER.info("Directory " + fileComp + " created.");
        }

    }

    /**
     * helpDoPost: Auxiliar method to handle get and post requests.
     * Download capabilities selectioned previously compressed in a tar.gz file
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void helpDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            checkFolders();

            String listParam = request.getParameter("listParameters");
            if (listParam == null || listParam.equals("") || listParam.equals("empty")) {
                request.setAttribute("messageError", "Please select a minimun of one capability to download");
                RequestDispatcher dispatcher = request.getRequestDispatcher("download.jsp");
                dispatcher.forward(request, response);
            } else {


                String[] listCapabilities = listParam.split(",");


                List<String> names = new ArrayList();
                List<File> listFiles = new ArrayList();
                for (int i = 0; i < listCapabilities.length; i++) {
                    names.add(listCapabilities[i]);
                    String filePath = getServletContext().getRealPath("/files") + '/' + names.get(i).toLowerCase() + ".tar.gz" + '/' + names.get(i).toLowerCase();
                    LOGGER.info("Nombre cap: " + names.get(i));
                    listFiles.add(new File(filePath));

                    //Increments downloadTimes index of each capability downloaded
                    String query = "SELECT timesDownloaded from capabilities WHERE name=" + "'" + listCapabilities[i] + "'";
                    Statement smt = connection.createStatement();
                    ResultSet result = smt.executeQuery(query);

                    while (result.next()) {
                        PreparedStatement queryTimes = connection.prepareStatement("UPDATE capabilities set timesDownloaded = ? WHERE name = ?");
                        queryTimes.setInt(1, ((result.getInt("timesDownloaded")) + 1));
                        queryTimes.setString(2, listCapabilities[i]);
                        queryTimes.executeUpdate();
                        queryTimes.close();
                    }
                }

                String fileTemp = getServletContext().getRealPath("/files") + "/temp/";
                String filePath = fileTemp + "compress";

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);
                String time = String.valueOf(hour) + String.valueOf(minutes) + String.valueOf(seconds);


                for (int i = 0; i < listFiles.size(); i++) {
                    File fileDestTemp = new File(filePath + '/' + names.get(i));
                    copyDirectory(listFiles.get(i), fileDestTemp);
                }
                String aux = "capabilities" + time + ".tar.gz";
                String nameFile = fileTemp + aux;
                createTarGzOfDirectory(filePath, nameFile);


                FileInputStream fileToDownload = new FileInputStream(nameFile);
                ServletOutputStream out = response.getOutputStream();
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=" + aux);
                response.setContentLength(fileToDownload.available());
                int c;
                while ((c = fileToDownload.read()) != -1) {
                    out.write(c);
                }
                out.flush();
                out.close();
                fileToDownload.close();



            }


        } catch (Exception ex) {
            LOGGER.severe("Exception: " + ex.getMessage());
            handleError(request, response);
        }
    }

    /**
     * copyDirectory: Copy one directory to other one
     * @param srcDir
     * @param dstDir
     * @throws IOException
     */
    private void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }

            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(srcDir, children[i]),
                        new File(dstDir, children[i]));
            }
        } else {

            copy(srcDir, dstDir);
        }
    }

    /**
     * copy: Copy one file
     * @param src
     * @param dst
     * @throws IOException
     */
    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);


        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Creates a tar.gz file at the specified path with the contents of the specified directory.
     *
     * @param dirPath The path to the directory to create an archive of
     * @param archivePath The path to the archive to create
     *
     * @throws IOException If anything goes wrong
     */
    public static void createTarGzOfDirectory(String directoryPath, String tarGzPath) throws IOException {
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        GzipCompressorOutputStream gzOut = null;
        TarArchiveOutputStream tOut = null;

        try {
            fOut = new FileOutputStream(new File(tarGzPath));
            bOut = new BufferedOutputStream(fOut);
            gzOut = new GzipCompressorOutputStream(bOut);
            tOut = new TarArchiveOutputStream(gzOut);

            addFileToTarGz(tOut, directoryPath, "");
        } catch (Exception ex) {
            LOGGER.severe("Exception: " + ex.getMessage());
        } finally {
            tOut.finish();
            tOut.close();
            gzOut.close();
            bOut.close();
            fOut.close();
        }
    }

    /**
     * Creates a tar entry for the path specified with a name built from the base passed in and the file/directory
     * name.  If the path is a directory, a recursive call is made such that the full directory is added to the tar.
     *
     * @param tOut The tar file's output stream
     * @param path The filesystem path of the file/directory being added
     * @param base The base prefix to for the name of the tar file entry
     *
     * @throws IOException If anything goes wrong
     */
    private static void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base) throws IOException {
        File f = new File(path);
        String entryName = base + f.getName();
        TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);

        tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tOut.putArchiveEntry(tarEntry);

        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);
            tOut.closeArchiveEntry();
        } else {
            tOut.closeArchiveEntry();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
    }

    private void listAll(File entry, List<File> listFile) throws java.io.IOException {
        try {
            if (!entry.exists()) {
                System.out.println(entry.getName() + " not found.");
                return;
            }
            if (entry.isFile()) {
                //  System.out.println(entry.getCanonicalPath());
                listFile.add(entry);
            } else if (entry.isDirectory()) {
                String[] fileName = entry.list();
                if (fileName == null) {
                    return;
                }
                for (int i = 0; i < fileName.length; i++) {
                    File item = new File(entry.getPath(), fileName[i]);
                    listAll(item, listFile);
                }
            }

        } catch (Exception ex) {
            LOGGER.severe("Exception: " + ex.getMessage());
        }


    }

    private void handleError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
        request.getSession().setAttribute("criticalMessageError", "An error has ocurred, please contact with the webmaster");
        dispatcher.forward(request, response);


    }
}
