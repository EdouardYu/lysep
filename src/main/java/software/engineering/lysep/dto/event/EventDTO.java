package software.engineering.lysep.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import software.engineering.lysep.dto.user.FullnameDTO;
import software.engineering.lysep.entity.Module;

import java.time.Instant;

@Data
@Builder
public class EventDTO {
    private String title;
    private String description;
    private Instant date;
    private Module module;
    private FullnameDTO publisher;
    @JsonProperty("created_at")
    private Instant createdAt;
}

