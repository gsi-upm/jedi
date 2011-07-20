

package userdata;

import java.util.Date;

/**
 *
 * @author nachomv
 */
public class Capabilities {

    private String name;
    private String id;
    private Date date;
    private String userUpload;

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public Date getDate(){
        return date;
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
    public void setDate( Date date ){
        this.date = date;
    }
    public void setUserUpload( String userUpload ){
        this.userUpload = userUpload;
    }
    

}
