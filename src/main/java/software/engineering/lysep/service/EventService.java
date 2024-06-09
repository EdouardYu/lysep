package software.engineering.lysep.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.engineering.lysep.dto.event.EventDTO;
import software.engineering.lysep.entity.*;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.repository.*;

import java.time.Instant;
import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final ModuleService moduleService;
    private final UserService userService;


    public void createEvent(EventDTO eventDTO) {
        Module module = this.moduleService.findById(eventDTO.getModuleId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event event = Event.builder()
            .title(eventDTO.getTitle())
            .description(eventDTO.getDescription())
            .date(eventDTO.getDate())
            .module(module)
            .publisher(user)
            .createdAt(Instant.now())
            .build();

        this.eventRepository.save(event);

        List<StudentModule> studentModules = this.moduleService
            .findAllStudentModulesByModuleId(eventDTO.getModuleId());


    }

    public Event updateEvent(int id, EventDTO eventDTO) {
        return null;
    }

    public void deleteEvent(int id) {
    }

    public List<Event> getAllEvents() {
        return null;
    }

    public Event getEventById(int id) {
        return null;
    }
}
