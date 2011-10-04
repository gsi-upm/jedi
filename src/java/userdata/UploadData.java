package userdata;

import java.io.File;
import java.io.IOException;
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
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.ice.tar.TarArchive;
import javax.servlet.http.HttpSession;
import java.util.*;



import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class UploadData extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static final String TMP_DIR_PATH = "/tmp";
    private File tmpDir;
    private static final String DESTINATION_DIR_PATH = "/files";
    private File destinationDir;

    /**
     * It looks if folder 'files' exists and inits the servlet
     * @param config
     * @throws ServletException
     */
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

    /**
     * Handle GET requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            helpGetPost(request, response);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    /**
     * Handle POST requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            helpGetPost(request, response);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    /**
     * Handles GET and POST requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void helpGetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            request.setCharacterEncoding("UTF-8");
            
            String comments = "";
            String nameCap = "";
            String fileSelected = "";
            String nameFolder = "";
            String listTagsTemp = "";

            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            fileItemFactory.setSizeThreshold(10 * 1024 * 1024); //10 MB
            fileItemFactory.setRepository(tmpDir);
            ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
            try {
                Capabilities c = new Capabilities();
                List items = uploadHandler.parseRequest(request);
                Iterator itr = items.iterator();
                while (itr.hasNext()) {
                    FileItem item = (FileItem) itr.next();
                    if (!item.isFormField()) {
                        if (!item.getName().equals("")) {
                            //Creates a folder and decompress the tar.gz file
                            File file = new File(destinationDir, item.getName());
                            file.mkdir();
                            String folderFile = getServletContext().getRealPath(DESTINATION_DIR_PATH + File.separator + item.getName());
                            File fileTwo = new File(folderFile, item.getName());
                            nameFolder = fileTwo.getName();
                            item.write(fileTwo);

                            String nameCapTemp = fileTwo.getName();
                            int posSufix = nameCapTemp.indexOf(".tar.gz");
                            if (posSufix != -1) {
                                nameCapTemp = nameCapTemp.substring(0, posSufix);
                            }
                            c.setName(nameCapTemp);


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
                            c.setNameFile(nameFolder);
                            //Takes a list of java files associated
                            infoCapabilitie(file, c);
                        } else {
                            RequestDispatcher dispatcher = request.getRequestDispatcher("Database" + "?" + "action" + "=" + "emptyFile");
                            LOGGER.severe("Redirecting");
                            request.getSession().setAttribute("capabilitie", c);
                            dispatcher.forward(request, response);

                        }
                        //Takes info from forms
                    } else {
                        String name = item.getFieldName();
                        String value = item.getString();
                        if (name.equals("comments")) {
                            comments = value;
                            if (comments.equals("")) {
                                comments = "Empty description";
                            }
                            c.setComments(comments);
                        } else if (name.equals("nameCap")) {
                            nameCap = value;
                            c.setName(name);
                        } else if (name.equals("uploadfile")) {
                            fileSelected = value;
                        } //Tags added by user
                        else if (name.equals("listTags")) {
                            listTagsTemp = value;                                                       
                            String tagsUser[] = listTagsTemp.split(";");
                            for (int i = 0; i < tagsUser.length; i++) {
                                c.addKeyWord(tagsUser[i]);
                            }
                        }
                    }
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("Database" + "?" + "action" + "=" + "saveData");
                LOGGER.severe("Redirecting");
                request.getSession().setAttribute("messageError", "");
                request.getSession().setAttribute("capabilitie", c);
                dispatcher.forward(request, response);


            } catch (FileUploadException ex) {
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
                    getInfoCap(file, cap);
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

    /**
     * getInfoCap: Looks for the key words in XML files
     * @param file
     * @return
     * @throws Exception
     */
    private void getInfoCap(File file, Capabilities cap) throws Exception {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(file);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);


            List<String> interestingWords = new ArrayList<String>();
            interestingWords.add("achievegoal");
            interestingWords.add("plan");
            interestingWords.add("application");

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    for (int i = 0; i < interestingWords.size(); i++) {
                        if ((startElement.getName().getLocalPart()).equals(interestingWords.get(i))) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            while (attributes.hasNext()) {
                                Attribute attribute = attributes.next();
                                if (attribute.getName().toString().equals("name")) {
                                    //LOGGER.info("Goal Name: " + attribute.getValue());
                                    String keyWord = attribute.getValue();
                                    keyWord = keyWord.replace("_", " ");
                                    cap.addKeyWord(keyWord);
                                }
                            }
                        }

                    }
                }
            }


        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());

        }

    }
}



