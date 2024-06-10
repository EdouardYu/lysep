package software.engineering.lysep.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@AllArgsConstructor
@Data
@Builder
public class EventCreationDTO {
    private String title;
    private String description;
    private Instant date;
    @JsonProperty("module_id")
    private int moduleId;
}

