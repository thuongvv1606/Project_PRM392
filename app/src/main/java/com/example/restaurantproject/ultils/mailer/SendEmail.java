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
    // Tag để log thông tin
    private static final String TAG = "SendEmail";

    // Phương thức gửi email
    public static void sendEmail(final String toEmail, final String subject, final String body) {
        // Tạo và bắt đầu một luồng mới để gửi email
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Thiết lập các thuộc tính cho kết nối SMTP
                    Properties props = new Properties();
                    props.put("mail.smtp.host", AppData.Gmail_Host);
                    props.put("mail.smtp.port", "465");
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.auth", "true");

                    // Tạo phiên gửi email với xác thực email, pass
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(AppData.Sender_Email_Address, AppData.Sender_Email_Password);
                                }
                            });

                    // Tạo tin nhắn email
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
        // Bắt đầu luồng gửi email
        thread.start();
    }
}
