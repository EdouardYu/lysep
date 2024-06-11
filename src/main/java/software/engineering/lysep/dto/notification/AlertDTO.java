package software.engineering.lysep.dto.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlertDTO {
    private String title;
    private String content;
}
