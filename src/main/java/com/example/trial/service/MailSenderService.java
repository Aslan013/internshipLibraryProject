package com.example.trial.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Service;

import com.example.trial.entity.User;
import com.example.trial.repository.UserRepository;

import java.util.Properties;

@Service
public class MailSenderService {

    private final UserRepository userRepository;

    public MailSenderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void sendNewPassword(User user) throws MessagingException {
        // 8 karakterli rastgele şifre oluştur
        String newPassword = randomPassword();

        // Veritabanını güncelle
        user.setPassword(newPassword);
        userRepository.save(user);

        // Mail ayarları
        String from = "aslann.013@gmail.com";
        String appPassword = "gqneskbdnneepnsc"; // Gmail uygulama şifresi

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, appPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        message.setSubject("Şifre Sıfırlama");
        message.setText("Sayın " + user.getUsername() + ",\nYeni şifreniz: " + newPassword +
                "\nBunu kullanarak giriş yaptıktan sonra profil kısmından şifrenizi değiştirebilirsiniz.");

        Transport.send(message);
    }

    private String randomPassword() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
        String password = "";
        for(int i = 0; i < 8; i++) {
            int idx = (int) (Math.random() * alphabet.length());
            password += alphabet.charAt(idx);
        }
        return password;
    }
}
