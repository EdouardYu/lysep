package software.engineering.lysep.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.engineering.lysep.dto.mapper.AlertMapper;
import software.engineering.lysep.dto.mapper.NotificationMapper;
import software.engineering.lysep.dto.notification.AlertDTO;
import software.engineering.lysep.dto.notification.NotificationDTO;
import software.engineering.lysep.entity.*;
import software.engineering.lysep.repository.AlertRepository;
import software.engineering.lysep.repository.NotificationRepository;
import software.engineering.lysep.repository.UserAlertRepository;
import software.engineering.lysep.repository.UserNotificationRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class NotificationService {
    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserAlertRepository userAlertRepository;
    private final AlertRepository alertRepository;
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
        String content = user.getFirstname() + " "
            + user.getLastname() + " a mis à jour l’évènement \"" + event.getTitle()
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

    public void sendEventAlert(Event event, List<Participant> participants, long daysUntilEvent) {
        String alertContent = "J-" + daysUntilEvent + " avant la deadline pour " + event.getTitle();

        Alert alert = Alert.builder()
            .title("Rappel pour l'événement " + event.getTitle())
            .content(alertContent)
            .event(event)
            .build();

        this.alertRepository.save(alert);

        List<UserAlert> userAlerts = participants.stream()
            .map(participant -> {
                UserAlert userAlert = UserAlert.builder()
                    .user(participant.getUser())
                    .alert(alert)
                    .build();

                this.sendEmailAlert(participant.getUser().getEmail(), alert);

                return userAlert;
            }).toList();

        this.userAlertRepository.saveAll(userAlerts);
    }

    @Async
    public void sendEmailAlert(String to, Alert alert) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.EMAIL);
        message.setTo(to);
        message.setSubject(alert.getTitle());
        message.setText(alert.getContent());
        this.javaMailSender.send(message);
    }

    public List<NotificationDTO> getUserNotifications() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userNotificationRepository
            .findAllByUserIdAndEventDateAfter(user.getId(), Instant.now())
            .map(NotificationMapper::toNotificationDTO)
            .toList();
    }

    public List<AlertDTO> getUserAlerts() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.userAlertRepository
            .findAllByUserIdAndEventDateAfter(user.getId(), Instant.now())
            .map(AlertMapper::toAlertDTO)
            .toList();
    }

    private String convertInstantToDate(Instant instant) {
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }
}
