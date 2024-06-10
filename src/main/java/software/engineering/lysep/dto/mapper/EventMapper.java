package software.engineering.lysep.dto.mapper;

import software.engineering.lysep.dto.event.LiteEventDTO;
import software.engineering.lysep.entity.Event;

public class EventMapper {
    public static LiteEventDTO toLiteEventDTO(Event event) {
        return LiteEventDTO.builder()
            .id(event.getId())
            .title(event.getTitle())
            .date(event.getDate())
            .module(event.getModule())
            .build();
    }
}
