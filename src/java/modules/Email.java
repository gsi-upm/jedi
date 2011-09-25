/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 *
 * @author nachomv
 */
public class Email extends HttpServlet implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Email.class.getName());
    private javax.mail.Message mail = null;

    public Email() {
        //You should find a correct smtp name
        this("smtp.gmail.com");
    }

    public Email(String hostName) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", hostName);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("maill.user","ignacio.mendizabal.vazquez@gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        

        Session session = Session.getDefaultInstance(properties, null);
        session.setDebug(true);

        mail = new MimeMessage(session);
    }

    public void setFromAddress(String fromName, String fromAddress) {
        try {
            InternetAddress from = new InternetAddress(fromAddress, fromName);
            mail.setFrom(from);
        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());

        }
    }

    public void setToAddress( String toName, String toAddress ){
      try{
        InternetAddress to = new InternetAddress( toAddress, toName );
        mail.addRecipient( Message.RecipientType.TO, to);
      } catch( Exception ex ){
          LOGGER.info("Exception: " + ex.getMessage() );
      }

    }

    public void setSubject(String subject) {
        try {
            mail.setSubject(subject);
        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());

        }
    }

    public void setBody(String body) {
        try {
            mail.setContent(body, "text/plain");
        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());
        }

    }

    public void send() {
        try {
            (new Thread(this)).start();
            return;

        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());
        }
    }

    public void run() {
        try {
            Transport transport = t
            Transport.send(mail);
        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());
        }
    }

    public void Main() {
        try {

            Email test = new Email("smtp.gmail.com");
            test.setFromAddress("Nacho", "ignacio.mendizabal.vazquez@gmail.com");
            test.setToAddress("Nacho", "ignacio.mendizabal.vazquez@gmail.com");
            test.setSubject("Hello World!!");
            test.setBody("Hellooooo!!!");
           test.send();




        } catch (Exception ex) {
            LOGGER.info("Exception: " + ex.getMessage());
        }
    }


    @Override
    public void init( ServletConfig conf ) throws ServletException{
        super.init(conf);

    }

    @Override
    public void destroy(){
        try{

        } catch( Exception ex ){
            LOGGER.info("Exception: " + ex.getMessage());
            
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        petitionAux( request, response );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        petitionAux( request, response );
    }


    private void petitionAux( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        Main();
    }


}
