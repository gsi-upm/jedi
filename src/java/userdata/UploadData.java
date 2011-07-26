package userdata;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;



import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.util.zip.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.ice.tar.TarArchive;
import javax.servlet.http.HttpSession;


import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;
import javazoom.upload.MultipartFormDataRequest;

public class UploadData extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static final String TMP_DIR_PATH = "/tmp";
    private File tmpDir;
    private static final String DESTINATION_DIR_PATH = "/files";
    private File destinationDir;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        tmpDir = new File(TMP_DIR_PATH);
        if (!tmpDir.isDirectory()) {
            throw new ServletException(TMP_DIR_PATH + " is not a directory");
        }
        String realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH);
        destinationDir = new File(realPath);
        if (!destinationDir.isDirectory()) {
            throw new ServletException(DESTINATION_DIR_PATH + " is not a directory");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            helpGetPost(request, response);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            helpGetPost(request, response);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    private void helpGetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, javazoom.upload.UploadException {
        response.setContentType("text/html");
        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            request.setCharacterEncoding("UTF-8");
            String comments = "";
            String nameCap = "";
            String fileSelected = "";

            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            fileItemFactory.setSizeThreshold(10 * 1024 * 1024); //10 MB
            fileItemFactory.setRepository(tmpDir);
            ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
            try {
                Capabilities c = new Capabilities("", "", null, "", "", "", null);
                List items = uploadHandler.parseRequest(request);
                Iterator itr = items.iterator();
                while (itr.hasNext()) {
                    FileItem item = (FileItem) itr.next();
                    if (!item.isFormField()) {
                        //Creates a folder and decompress the tar.gz file
                        File file = new File(destinationDir, item.getName());
                        file.mkdir();
                        String folderFile = getServletContext().getRealPath(DESTINATION_DIR_PATH + File.separator + item.getName());
                        File fileTwo = new File(folderFile, item.getName());
                        item.write(fileTwo);
                        UploadData u = new UploadData();
                        u.unTarGz(fileTwo, folderFile);

                        //Takes info from the user
                        HttpSession session = request.getSession(true);
                        User user = (User) session.getValue("validUser");

                        String userName = user.getUser();
                        Date date = new Date();
                        java.sql.Date actualDate = new java.sql.Date(date.getTime());

                        //Time of upload
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        int seconds = calendar.get(Calendar.SECOND);
                        String timeUpload = String.valueOf(hour) + ':' + String.valueOf(minutes) + ':' + String.valueOf(seconds);

                        //Form info
                        List<File> listFiles = new ArrayList<File>();
                        c.setDate(actualDate);
                        c.setTimeUpload(timeUpload);
                        c.setListFile(listFiles);
                        c.setUserUpload(userName);
                        
                        //Takes the name of the capabilitiy and java files assciated
                        infoCapabilitie(file, c);

                        RequestDispatcher dispatcher = request.getRequestDispatcher("Database" + "?" + "action" + "=" + "saveData");

                        LOGGER.severe("Redirecting");
                        request.getSession().setAttribute("capabilitie", c);
                        dispatcher.forward(request, response);

                    } else {
                        String name = item.getFieldName();
                        String value = item.getString();
                        if (name.equals("comments")) {
                            comments = value;
                            c.setComments(comments);
                        } else if (name.equals("nameCap")) {
                            nameCap = value;
                            c.setName(name);
                        } else if (name.equals("uploadfile")) {
                            fileSelected = value;

                        }

                    }

                }


            } catch (FileUploadException ex) {
                System.out.println("Exception: " + ex.getMessage());
            } catch (javazoom.upload.UploadException ex) {
                System.out.println("Exception: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }



        }
    }

    /**
     * unTarGz: Decompress tar.gz files
     * @param zipPath
     * @param unZipPath
     * @throws Exception
     * //http://everac99.wordpress.com/
     */
    private void unTarGz(File zipPath, final String unZipPath) throws Exception {
        GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(zipPath));

        String tempDir = unZipPath.substring(0, unZipPath.lastIndexOf('/'));
        String tempFile = "" + new Date().getTime() + ".tmp";
        String tempPath = tempDir + "/" + tempFile;

        OutputStream out = new FileOutputStream(tempPath);

        byte[] data = new byte[1024];
        int len;
        while ((len = gzipInputStream.read(data)) > 0) {
            out.write(data, 0, len);
        }

        gzipInputStream.close();
        out.close();

        TarArchive tarArchive = new TarArchive(new FileInputStream(tempPath));
        tarArchive.extractContents(new File(unZipPath));
        tarArchive.closeArchive();
        new File(tempPath).delete();

    }

    /**
     * Get information from capabilities files.
     * TODO: Use this to get for information than only the name
     * @param file
     * @return
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private void infoCapabilitie(File file, Capabilities cap) throws java.io.IOException, java.lang.Exception {
        try {

            if (file.isFile()) {
                String filePath = file.getCanonicalPath();
                if (filePath.endsWith(".xml")) {
                    String nameCap = getNameCap(file);
                    cap.setName(nameCap);
                    LOGGER.severe("File saved as: " + nameCap);
                } else if (filePath.endsWith(".java")) {
                    cap.addListFile(file);
                }
            } else if (file.isDirectory()) {
                String[] fileName = file.list();
                if (fileName != null) {
                    for (int i = 0; i < fileName.length; i++) {
                        File item = new File(file.getPath(), fileName[i]);
                        infoCapabilitie(item, cap);
                    }
                }
            }



        } catch (java.io.IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        } catch (java.lang.Exception ex) {
            System.out.println("Exception: " + ex.getMessage());

        }


    }

    private String getNameCap(File file) throws Exception {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            Element rootElement = document.getDocumentElement();
            String capabilityName = rootElement.getAttribute("name");
            return capabilityName;

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return (ex.getMessage());
        }

    }
}


