package software.engineering.lysep.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.engineering.lysep.entity.Notification;
import software.engineering.lysep.entity.User;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationId implements Serializable {
    private User user;
    private Notification notification;
}