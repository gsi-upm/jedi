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
import java.util.Date;
import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;

import java.util.logging.Logger;
import org.apache.commons.compress.compressors.gzip.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

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

/**
 * Check if exist necessary folders to compress and download files
 */
    private void checkFolders(){
        String fileTemp = getServletContext().getRealPath("/files") + "/temp";
        File fileT = new File(fileTemp);
        if(!fileT.isDirectory()){
            fileT.mkdir();
            LOGGER.info("Directory " + fileTemp + " created.");
        }
        String fileCompress = fileTemp + "/compress";
        File fileComp = new File(fileCompress);
        if(!fileComp.isDirectory()){
            fileComp.mkdir();
            LOGGER.info("Directory " + fileComp + " created.");
        }

    }

    
    private void helpDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            checkFolders();

            String listParam = request.getParameter("listParameters");
            String [] listCapabilities = listParam.split(",");

            
            List<String> names = new ArrayList();
            List<File> listFiles = new ArrayList();
            for(int i=0;i<listCapabilities.length;i++){
                names.add(listCapabilities[i]);
                String filePath = getServletContext().getRealPath("/files") + '/' + names.get(i).toLowerCase() + ".tar.gz" + '/' + names.get(i).toLowerCase();
                listFiles.add(new File(filePath));
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
            createTarGzOfDirectory(filePath, fileTemp + "capabilities" + time + ".tar.gz");

        } catch (Exception ex) {
            LOGGER.severe("Exception: " + ex.getMessage());
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
}
