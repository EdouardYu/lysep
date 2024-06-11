package software.engineering.lysep.dto.event;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
@Builder
public class EventModificationDTO {
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Date cannot be null")
    @FutureOrPresent(message = "Date cannot be in the past")
    private Instant date;

}

