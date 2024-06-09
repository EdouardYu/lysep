package software.engineering.lysep.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import software.engineering.lysep.dto.validator.ValidRole;
import software.engineering.lysep.entity.enumeration.Role;

@Data
public class RegistrationDTO {
    private String email;

    @NotBlank(message = "Firstname cannot be empty")
    @Size(max = 64, message = "Firstname must at most 64 characters long")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Firstname can only contain letters, spaces, hyphens, and apostrophes")
    private String firstname;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 64, message = "Lastname must be at most 64 characters long")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Lastname can only contain letters, spaces, hyphens, and apostrophes")
    private String lastname;

    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters long")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%&*+<=>?@^_-]).*$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, " +
            "one number, and one special character (! # $ % & * + - < = > ? @ ^ _)"
    )
    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(
        regexp = "^\\(\\+\\d{1,3}\\)\\d{1,12}$",
        message = "Phone number must start with a country code in the format (+123) followed by up to 12 digits"
    )
    private String phone;

    @NotNull(message = "Role cannot be null")
    @ValidRole
    private Role role;

    @JsonCreator
    public RegistrationDTO(
        String firstname,
        String lastname,
        String password,
        String phone,
        Role role
        ) {
        this.firstname = firstname == null ? null : firstname.trim();
        this.lastname = lastname == null ? null : lastname.trim();
        this.password = password;
        this.phone = phone;
        this.role = role;

        StringBuilder email = new StringBuilder();
        email.append(this.firstname);
        email.append(".");
        email.append(this.lastname);
        email.append("@");
        if(Role.STUDENT.equals(role)) email.append("eleve.");
        email.append("isep.fr");

        this.email = email.toString().toLowerCase();
        this.username = this.firstname + " " + this.lastname;
    }
}
