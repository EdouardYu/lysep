package software.engineering.lysep.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.engineering.lysep.dto.event.EventCreationDTO;
import software.engineering.lysep.dto.event.EventDTO;
import software.engineering.lysep.dto.event.EventModificationDTO;
import software.engineering.lysep.dto.event.LiteEventDTO;
import software.engineering.lysep.service.EventService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("events")
public class EventController {
    private final EventService eventService;

    @PreAuthorize("hasAnyRole('PROFESSOR', 'SCHOOL_ADMINISTRATOR', 'APPLICATION_ADMINISTRATOR')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createEvent(@Valid @RequestBody EventCreationDTO eventCreationDTO) {
        this.eventService.createEvent(eventCreationDTO);
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'SCHOOL_ADMINISTRATOR', 'APPLICATION_ADMINISTRATOR')")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(
        path = "{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public EventDTO updateEvent(@PathVariable int id, @Valid @RequestBody EventModificationDTO eventDTO) {
        return this.eventService.updateEvent(id, eventDTO);
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'SCHOOL_ADMINISTRATOR', 'APPLICATION_ADMINISTRATOR')")
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(path = "{id}")
    public void deleteEvent(@PathVariable int id) {
        this.eventService.deleteEvent(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LiteEventDTO> getAllEvents() {
        return this.eventService.getAllEvents();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EventDTO getEventById(@PathVariable int id) {
        return this.eventService.getEventById(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "{id}/permission", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean hasPermission(@PathVariable int id) {
        return this.eventService.hasPermission(id);
    }
}
