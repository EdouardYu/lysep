package software.engineering.lysep.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileModificationDTO {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 129, message = "Username must be between 3 and 128 characters long")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Username can only contain letters, spaces, hyphens, and apostrophes")
    private String username;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(
        regexp = "^\\(\\+\\d{1,3}\\)\\d{1,12}$",
        message = "Phone number must start with a country code in the format (+123) followed by up to 12 digits"
    )
    private String phone;

    @JsonCreator
    public ProfileModificationDTO(
        String username,
        String phone
    ) {
        this.username = username == null ? null : username.trim();
        this.phone = phone;
    }
}
