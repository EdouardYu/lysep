package software.engineering.lysep.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailModificationDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    private String password;

    @JsonCreator
    public EmailModificationDTO(
        String email,
        String password
    ) {
        this.email = email == null ? null : email.toLowerCase();
        this.password = password;

    }
}
