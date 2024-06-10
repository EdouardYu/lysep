package software.engineering.lysep.dto.event;

import lombok.Builder;
import lombok.Data;
import software.engineering.lysep.entity.Module;

import java.time.Instant;

@Data
@Builder
public class LiteEventDTO {
    private int id;
    private String title;
    private Instant date;
    private Module module;
}

