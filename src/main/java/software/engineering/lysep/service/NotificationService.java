package software.engineering.lysep.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.engineering.lysep.entity.*;
import software.engineering.lysep.repository.NotificationRepository;
import software.engineering.lysep.repository.UserNotificationRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {
    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final String EMAIL = "no-reply@lysep.fr";

    public void sendActivationCodeEmail(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.EMAIL);
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Code d'activation Lys'ep");

        String text = "Voici le code d'activation pour créer votre compte Lys'ep\n"
            + validation.getCode()
            + "\nCe code n'est valable que pour 10 minutes";
        message.setText(text);

        this.javaMailSender.send(message);
    }

    public void sendPasswordResetEmail(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.EMAIL);
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Code de réinitialisation de mot de passe Lys'ep");

        String text = "Voici le code pour réinitialiser le mot de passe de votre compte Lys'ep\n"
            + validation.getCode()
            + "\nCe code n'est valable que pour 10 minutes";
        message.setText(text);

        this.javaMailSender.send(message);
    }

    public void sendEventCreationNotification(Event event, List<Participant> participants) {
        String content = event.getPublisher().getFirstname() + " "
            + event.getPublisher().getLastname() + " a créé l’évènement \"" + event.getTitle()
            + "\" le " + this.convertInstantToDate(event.getCreatedAt());

        Notification notification = Notification.builder()
            .title("Création de \"" + event.getTitle() + "\"")
            .content(content)
            .event(event)
            .build();

        this.notificationRepository.save(notification);

        List<UserNotification> userNotifications = participants.stream()
            .map(participant -> {
                UserNotification userNotification = UserNotification.builder()
                    .user(participant.getUser())
                    .notification(notification)
                    .build();

                this.sendEmailNotification(participant.getUser().getEmail(), notification);

                return userNotification;
            }).toList();

        this.userNotificationRepository.saveAll(userNotifications);
    }

    public void sendEventModificationNotification(User user, Event event, List<Participant> participants) {
        String content = event.getPublisher().getFirstname() + " "
            + event.getPublisher().getLastname() + " a mis à jour l’évènement \"" + event.getTitle()
            + "\" le " + this.convertInstantToDate(Instant.now());

        Notification notification = Notification.builder()
            .title("Mise à jour de \"" + event.getTitle() + "\"")
            .content(content)
            .event(event)
            .build();

        this.notificationRepository.save(notification);

        List<UserNotification> userNotifications = participants.stream()
            .map(participant -> {
                UserNotification userNotification = UserNotification.builder()
                    .user(participant.getUser())
                    .notification(notification)
                    .build();

                this.sendEmailNotification(participant.getUser().getEmail(), notification);

                return userNotification;
            }).toList();

        this.userNotificationRepository.saveAll(userNotifications);
    }

    public void sendEventDeletionNotification(User user, Event event, List<Participant> participants) {
        String content = user.getFirstname() + " "
            + user.getLastname() + " a supprimé l’évènement \"" + event.getTitle()
            + "\" le " + this.convertInstantToDate(Instant.now());

        Notification notification = Notification.builder()
            .title("Suppression de \"" + event.getTitle() + "\"")
            .content(content)
            .event(event)
            .build();

        this.notificationRepository.save(notification);

        List<UserNotification> userNotifications = participants.stream()
            .map(participant -> {
                UserNotification userNotification = UserNotification.builder()
                    .user(participant.getUser())
                    .notification(notification)
                    .build();

                this.sendEmailNotification(participant.getUser().getEmail(), notification);

                return userNotification;
            }).toList();

        this.userNotificationRepository.saveAll(userNotifications);
    }

    @Async
    public void sendEmailNotification(String to, Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.EMAIL);
        message.setTo(to);
        message.setSubject(notification.getTitle());
        message.setText(notification.getContent());
        this.javaMailSender.send(message);
    }

    private String convertInstantToDate(Instant instant) {
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }
}
