package software.engineering.lysep.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationDTO {
    private String email;
    private String password;

    @JsonCreator
    public AuthenticationDTO(String email, String password) {
        this.email = email == null ? null : email.toLowerCase();
        this.password = password;
    }
}
