package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 12/26/2016.
 */

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Yahoo_attachment_kot_can {

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.mail.yahoo.com";
    // final String fromUser = "giftvincy@gmail.com";
    // final String fromUserEmailPassword = "jk2008gv";

    String fromEmail;
    String fromPassword;
    List<String> toEmailList;
    String emailSubject;
    String emailBody;


    String currentDateandTimee1;
    String timee1;


    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public Yahoo_attachment_kot_can() {

    }

    public Yahoo_attachment_kot_can(String fromEmail, String fromPassword,
                                    List<String> toEmailList, String emailSubject, String emailBody, String currentDateandTimee1, String timee1) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        this.currentDateandTimee1 = currentDateandTimee1;
        this.timee1 = timee1;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.host", emailHost);
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("Yahoo", "Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (String toEmail : toEmailList) {
            Log.i("Yahoo","toEmail: "+toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));
        }


        BodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        messageBodyPart.setText(emailBody);
        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
//        String filename = "sdcard/Download/IvePOS_items_report"+currentDateandTimee1+"_"+timee1+".csv";
        String filename = "sdcard/IVEPOS_reports/IVEPOS_kot_cancel_report/IvePOS_kot_cancel_report"+currentDateandTimee1+"_"+timee1+".csv";
//        String filename = "sdcard/IVEPOS_reports/IVEPOS_product_report/IvePOS_product_report"+currentDateandTimee1+"_"+timee1+".csv";
//        String filename = "sdcard/IVEPOS_reports/IVEPOS_customer_list/IvePOS_customer_list"+currentDateandTimee1+"_"+timee1+".csv";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        emailMessage.setText(emailBody);
        emailMessage.setContent(multipart);
        emailMessage.setSubject(emailSubject);

//        emailMessage.setSubject(emailSubject);
//        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("Yahoo", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws MessagingException {

//        try {
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("Yahoo","allrecipients: "+emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("Yahoo", "Email sent successfully.");
//        }catch (Exception e){
//            Log.i("GMail", "Email not sent.");
////            AlertDialogog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        }

    }

}

