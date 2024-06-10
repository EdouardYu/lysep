package software.engineering.lysep.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
@Builder
public class EventModificationDTO {
    private String description;
    private Instant date;

}

