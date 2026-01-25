package app.springdev.scheduler.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
@Deprecated
@Component
@RequiredArgsConstructor
public class MailAlertSender implements AlertSender {

    private final JavaMailSender mailSender;

    @Override
    public void send(String title, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("ops@company.com");
        mail.setSubject(title);
        mail.setText(message);
        mailSender.send(mail);
    }
}
