package software.engineering.lysep.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import software.engineering.lysep.entity.Validation;

@AllArgsConstructor
@Service
public class NotificationService {
    private final JavaMailSender javaMailSender;

    public void sendActivationCodeEmail(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@lysep.fr");
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Lys'ep activation code");

        String text = "Here's the activation code to create your Lys'ep activation account\n"
            + validation.getCode()
            + "\nThis code is only valid for 10 minutes";
        message.setText(text);

        this.javaMailSender.send(message);
    }

    public void sendPasswordResetEmail(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@lysep.fr");
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Lys'ep password reset code");

        String text = "Here's the code to reset your Lys'ep account password\n"
            + validation.getCode()
            + "\nThis code is only valid for 10 minutes";
        message.setText(text);

        this.javaMailSender.send(message);
    }
}
