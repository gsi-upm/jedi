package userdata;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class UploadData extends HttpServlet {

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
                    Date date = new Date();
                    


                    String folderFile = getServletContext().getRealPath(DESTINATION_DIR_PATH + File.separator + item.getName());
                    File fileTwo = new File(folderFile, item.getName());
                    item.write(fileTwo);

                    
                    UploadData u = new UploadData();
                    u.unTarGz(fileTwo, folderFile);

                    
                }
            }
        } catch (FileUploadException ex) {
            log("Error encountered while parsing the request", ex);
        } catch (Exception ex) {
            log("Error encountered while uploading file", ex);
        }
    }


    /**
     * unTarGz: Decompress tar.gz files
     * @param zipPath
     * @param unZipPath
     * @throws Exception
     * //http://everac99.wordpress.com/
     */
    private void unTarGz( File zipPath, final String unZipPath) throws Exception {
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

        //…Y luego mediante el paquete com.ice.tar extraemos la informacion del .tar temporal
        TarArchive tarArchive = new TarArchive(new FileInputStream(tempPath));
        tarArchive.extractContents(new File(unZipPath));
        tarArchive.closeArchive();

        //Borrado del archivo remporal (.tar)
        new File(tempPath).delete();
    }
}
