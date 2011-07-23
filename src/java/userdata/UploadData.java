package userdata;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
import java.util.Date;
import com.ice.tar.TarArchive;
import javax.servlet.http.HttpSession;

import javax.xml.parsers.*;
import org.w3c.dom.*;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setSizeThreshold(10 * 1024 * 1024); //10 MB
        fileItemFactory.setRepository(tmpDir);
        ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
        try {
            List items = uploadHandler.parseRequest(request);
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (!item.isFormField()) {
                    File file = new File(destinationDir, item.getName());
                    file.mkdir();
                    String folderFile = getServletContext().getRealPath(DESTINATION_DIR_PATH + File.separator + item.getName());
                    File fileTwo = new File(folderFile, item.getName());
                    item.write(fileTwo);

                    UploadData u = new UploadData();
                    u.unTarGz(fileTwo, folderFile);

                    //
                    infoCapabilitie(file);

                    //Takes info from the user
                    HttpSession session = request.getSession(true);
                    User user = (User) session.getValue("validUser");

                    String userName = user.getUser();
                    Date date = new Date();

                    java.sql.Date actualDate = new java.sql.Date(date.getTime());
                    //Form info
                    String comments = request.getParameter("comments");
                //    String nameCap = request.getParameter("nameCap");
              //      String nameCap = infoCapabilitie(fileTwo);
                  //  System.out.println("Nombre: " + nameCap);



                    //Random ID for each capabilitie uploaded
                    Random r = new Random();
                    double tempInt = r.nextDouble();
                    tempInt = tempInt * 100000;
                    tempInt = Math.round(tempInt);
                    String id = String.valueOf((int) tempInt);




                    Capabilities c = new Capabilities(nameCap, id, actualDate, userName, comments);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("Database" + "?" + "action" + "=" + "saveData");
                    request.getSession().setAttribute("capabilitie", c);
                    dispatcher.forward(request, response);
                }
            }
        } catch (FileUploadException ex) {
            System.out.println("Exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
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
    private void infoCapabilitie(File file) throws java.io.IOException, java.lang.Exception {
        try {
            
            if (file.isFile()) {
                String filePath = file.getCanonicalPath();
                if (filePath.endsWith(".xml")) {
                  String nameCap =  getNameCap(file);               
                }
            } else if (file.isDirectory()) {
                String[] fileName = file.list();
                if (fileName != null) {                
                for (int i = 0; i < fileName.length; i++) {
                    File item = new File(file.getPath(), fileName[i]);
                    infoCapabilitie(item);
                }
            }          
        }
         
          
        }

        catch (java.io.IOException ex) {
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
            return(ex.getMessage());
        }
          
    }

    private void getInfoCap(){
        
    }
}


