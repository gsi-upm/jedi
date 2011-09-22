/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userdata;

/**
 *
 * @author imendizabal
 */
public class User {

    private String user;
    private String email;
    private String password;
    private String repeatPassword;
    private String question;
    private String answer;

    public String getUser(){
        return user;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getRepeatPassword(){
        return repeatPassword;
    }
    public String getQuestion(){
        return question;
    }
    public String getAnswer(){
        return answer;
    }
    public void setUser( String user ){
        this.user = user;
    }
    public void setEmail( String email ){
        this.email = email;
    }
    public void setPassword( String password ){
        this.password = password;
    }
    public void setRepeatPassword( String repeatPassword ){
        this.repeatPassword = repeatPassword;
    }
    public void setQuestion( String question ){
        this.question = question;
    }
    public void setAnswer( String answer ){
        this.answer = answer;
    }


}
