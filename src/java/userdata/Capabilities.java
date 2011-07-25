

package userdata;

import java.util.List;
import java.io.File;

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


    public Capabilities( String name, String id, java.sql.Date date, String timeUpload, String userUpload, String comments, List <File> listFile ){
        this.name = name;
        this.id = id;
        this.dateUpload = date;
        this.userUpload = userUpload;
        this.comments = comments;
        this.listFile = listFile;
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

    public void addListFile( File file ){
        listFile.add(file);
    }

}
