package software.engineering.lysep.dto.mapper;

import software.engineering.lysep.dto.notification.NotificationDTO;
import software.engineering.lysep.entity.UserNotification;

public class NotificationMapper {
    public static NotificationDTO toNotificationDTO(UserNotification un) {
        return NotificationDTO.builder()
            .title(un.getNotification().getTitle())
            .content(un.getNotification().getContent())
            .build();
    }
}
