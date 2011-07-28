

package userdata;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nachomv
 */
public class Capabilities {

    private String name;
    private String id;
    private java.sql.Date dateUpload;
    private String timeUpload;
    private String userUpload;
    private String comments;
    private List <File> listFile;
    private String nameFile;

    public Capabilities(){
        this.name = "";
        this.id = "";
        this.dateUpload = new java.sql.Date(0);
        this.userUpload = "";
        this.comments = "";
        this.listFile = new ArrayList<File>();
        this.nameFile = "";
    }
    public Capabilities( String name, String id, java.sql.Date date, String timeUpload, String userUpload, String comments, List <File> listFile, String nameFile ){
        this.name = name;
        this.id = id;
        this.dateUpload = date;
        this.userUpload = userUpload;
        this.comments = comments;
        this.listFile = listFile;
        this.nameFile = nameFile;
    }
    
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public java.sql.Date getDate(){
        return dateUpload;
    }
    public String getTimeUpload(){
        return timeUpload;
    }

    public String getComments(){
        return comments;
    }
    public String getUserUpload(){
        return userUpload;
    }

    public List <File> getListFile(){
        return listFile;
    }

    public String getNameFile(){
        return nameFile;
    }

    public void setName( String name ){
        this.name = name;
    }
    public void setId( String id ){
        this.id = id;
    }
    public void setDate( java.sql.Date date ){
        this.dateUpload = date;
    }
    public void setTimeUpload( String timeUpload ){
        this.timeUpload = timeUpload;
    }

    public void setComments( String comments ){
        this.comments = comments;
    }
    public void setUserUpload( String userUpload ){
        this.userUpload = userUpload;
    }

    public void setListFile( List <File> listFile ){
        this.listFile = listFile;
    }

    public void setNameFile( String nameFile ){
        this.nameFile = nameFile;
    }

    public void addListFile( File file ){
        listFile.add(file);
    }

}
