/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userdata;

import com.ice.tar.TarEntry;
import com.ice.tar.TarGzOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;

import java.util.logging.Logger;
import org.apache.commons.compress.compressors.gzip.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.io.IOUtils;

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
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        GzipCompressorOutputStream gzOut = null;
        TarArchiveOutputStream tOut = null;

        try {
            List<String> fileList = new ArrayList();

            String fileTar = getServletContext().getRealPath("/files") + "/temp/fileCompressed.tar.gz";

            fileList.add("alarmclock");
            fileList.add("blackjack");



//   String filePathFirst = getServletContext().getRealPath("/files") + '/' + fileList.get(0) + ".tar.gz" + '/' + fileList.get(0);
            //       createTarGzOfDirectory(filePathFirst, fileTar);

            for (int i = 0; i < fileList.size(); i++) {
                String filePath = getServletContext().getRealPath("/files") + '/' + fileList.get(i) + ".tar.gz" + '/' + fileList.get(i);
                createTarGzOfDirectory(filePath, fileTar);

                fOut = new FileOutputStream(new File(fileTar));
                bOut = new BufferedOutputStream(fOut);
                gzOut = new GzipCompressorOutputStream(bOut);
                tOut = new TarArchiveOutputStream(gzOut);

                addFileToTarGz(tOut, filePath, "");



            }
        } catch (IOException ex) {
            LOGGER.severe("Exception: " + ex.getMessage());
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

    /*  public void tarGz(List filePaths, String zipPath) throws FileNotFoundException {

    FileInputStream inputStream = null;
    BufferedInputStream bufferedInputStream = null;

    FileOutputStream outputStream = null;
    TarGzOutputStream tarOutputStream = null;

    try {

    outputStream = new FileOutputStream(zipPath);
    tarOutputStream = new TarGzOutputStream(outputStream);

    byte data[] = new byte[1024];
    int dataSize = 0;

    for (Iterator t = filePaths.iterator(); t.hasNext();) {
    String cdrPath = "" + t.next();
    System.out.println("cdrPath: " + cdrPath );
    File fileTemp = new File(cdrPath);
    System.out.println("fileTemp y cdrPath: "  + fileTemp.getCanonicalPath());
    String cdrName = cdrPath;


    TarEntry entry = new TarEntry(cdrName);
    entry.setSize(fileTemp.length());
    entry.setName(cdrName);
    tarOutputStream.putNextEntry(entry);


    inputStream = new FileInputStream(cdrPath);
    bufferedInputStream = new BufferedInputStream(inputStream, data.length);

    while ((dataSize = bufferedInputStream.read(data, 0, data.length)) != -1) {
    tarOutputStream.write(data, 0, dataSize);

    }

    tarOutputStream.closeEntry();

    if (bufferedInputStream != null) {
    bufferedInputStream.close();
    }

    if (inputStream != null) {
    inputStream.close();
    }
    }
    } catch (FileNotFoundException ex) {
    LOGGER.severe("Exception: " + ex.getMessage());
    } catch (java.io.IOException ex) {
    LOGGER.severe("Exception: " + ex.getMessage());
    } catch (final Exception e) {
    LOGGER.severe("Exception");
    } finally {
    try {
    if (bufferedInputStream != null) {
    bufferedInputStream.close();
    }

    if (inputStream != null) {
    inputStream.close();
    }

    if (tarOutputStream != null) {
    tarOutputStream.close();
    }

    if (outputStream != null) {
    outputStream.close();
    }
    } catch (final IOException ioe) {
    LOGGER.severe("Resource release error");
    }
    }



    }*/
    private void listAll(File entry, List<File> listFile) throws java.io.IOException {
        try {
            if (!entry.exists()) {
                System.out.println(entry.getName() + " not found.");
                return;
            }
            if (entry.isFile()) {
                System.out.println(entry.getCanonicalPath());
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
