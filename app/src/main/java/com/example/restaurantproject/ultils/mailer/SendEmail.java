package com.example.restaurantproject.ultils.mailer;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    private static final String TAG = "SendEmail";

    public static void sendEmail(final String toEmail, final String subject, final String body) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", AppData.Gmail_Host);
                    props.put("mail.smtp.port", "465");
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.auth", "true");

                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(AppData.Sender_Email_Address, AppData.Sender_Email_Password);
                                }
                            });

                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(AppData.Sender_Email_Address, "Restaurant Management System"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                    message.setSubject(subject);
                    message.setText(body);

                    Transport.send(message);

                    Log.d(TAG, "Email sent successfully to " + toEmail);
                } catch (MessagingException e) {
                    Log.e(TAG, "Failed to send email", e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }
}
