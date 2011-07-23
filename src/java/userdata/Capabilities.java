

package userdata;

import java.util.Date;

/**
 *
 * @author nachomv
 */
public class Capabilities {

    private String name;
    private String id;
    private java.sql.Date date;
    private String userUpload;
    private String comments;


    public Capabilities( String name, String id, java.sql.Date date, String userUpload, String comments ){
        this.name = name;
        this.id = id;
        this.date = date;
        this.userUpload = userUpload;
        this.comments = comments;
    }
    
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public java.sql.Date getDate(){
        return date;
    }

    public String getComments(){
        return comments;
    }
    public String getUserUpload(){
        return userUpload;
    }

    public void setName( String name ){
        this.name = name;
    }
    public void setId( String id ){
        this.id = id;
    }
    public void setDate( java.sql.Date date ){
        this.date = date;
    }

    public void setComments( String comments ){
        this.comments = comments;
    }
    public void setUserUpload( String userUpload ){
        this.userUpload = userUpload;
    }
    

}
