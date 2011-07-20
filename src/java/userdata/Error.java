/**
 * Class userdata.Error
  */

package userdata;

/**
 *
 * @author imendizabal
 */

public class Error {

    private String messageError;

    public Error(){
        messageError = "";
    }

    public String getMessageError(){
        return messageError;
    }

    public void setMessageError( String messageError ){
           this.messageError += messageError + "<br>";
    }

}
