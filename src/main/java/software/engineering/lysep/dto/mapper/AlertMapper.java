package software.engineering.lysep.dto.mapper;

import software.engineering.lysep.dto.notification.AlertDTO;
import software.engineering.lysep.entity.UserAlert;

public class AlertMapper {
    public static AlertDTO toAlertDTO(UserAlert ua) {
        return AlertDTO.builder()
            .title(ua.getAlert().getTitle())
            .content(ua.getAlert().getContent())
            .build();
    }
}
