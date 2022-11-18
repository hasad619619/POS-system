/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASS;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Mayura Lakshan
 */
public class Email {

    String Email;
    String PW;

    public void sendMail(String All,String Profit,String Cost) {
        String SQL = "SELECT * FROM email WHERE id='1'";
        try {
            ResultSet RS =JDBC.getInstance().getData(SQL);
            if (RS.next()) {
                Email = RS.getString("email");
                PW = RS.getString("pw");
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat2 = new SimpleDateFormat("HH:MM");
        Calendar cal2 = Calendar.getInstance();
        String DATE = dateFormat.format(cal.getTime());
        String TIME = dateFormat2.format(cal2.getTime());

        final String username = Email; //ur email
        final String password = PW;

        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Email));//ur email
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Email));//u will send to
            message.setSubject("Thotagamuwa Grocery And Food City");
            message.setText(""
                    + "------------------------------------------------------------------\n"
                    + "     Daily Sales Report Genarated By System \n"
                    + "------------------------------------------------------------------\n"
                    + "\n"
                    + "Date:"+DATE
                    +"\n"
                    + "Time:"+TIME
                    + "\n"
                    + "\n"
                    + "All Sales-RS:"+All
                    +"\n"
                    + "Cost-RS:"+Cost
                    +"\n"
                    + "Profit-RS:"+Profit
            
                    );
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    

}
