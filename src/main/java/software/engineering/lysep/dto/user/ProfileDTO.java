package software.engineering.lysep.dto.user;

import lombok.Builder;
import lombok.Data;
import software.engineering.lysep.entity.enumeration.Role;

@Data
@Builder
public class ProfileDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String phone;
    private Role role;
}
