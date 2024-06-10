package software.engineering.lysep.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import software.engineering.lysep.dto.notification.NotificationDTO;
import software.engineering.lysep.service.NotificationService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NotificationDTO> getUserNotifications() {
        return notificationService.getUserNotifications();
    }
}
