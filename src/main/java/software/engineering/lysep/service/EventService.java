package software.engineering.lysep.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.engineering.lysep.dto.event.EventCreationDTO;
import software.engineering.lysep.dto.event.EventDTO;
import software.engineering.lysep.dto.event.EventModificationDTO;
import software.engineering.lysep.dto.event.LiteEventDTO;
import software.engineering.lysep.dto.mapper.EventMapper;
import software.engineering.lysep.dto.user.FullnameDTO;
import software.engineering.lysep.entity.*;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.entity.enumeration.Role;
import software.engineering.lysep.repository.*;
import software.engineering.lysep.service.exception.NotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ModuleService moduleService;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;

    public void createEvent(EventCreationDTO eventDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Role.PROFESSOR.equals(user.getRole())
            && !this.moduleService.isProfessorAssignedToModule(user.getId(), eventDTO.getModuleId()))
            throw new AccessDeniedException("Access denied");

        Module module = this.moduleService.findById(eventDTO.getModuleId());

        Event event = Event.builder()
            .title(eventDTO.getTitle())
            .description(eventDTO.getDescription())
            .date(eventDTO.getDate())
            .module(module)
            .publisher(user)
            .createdAt(Instant.now())
            .deleted(false)
            .build();

        this.eventRepository.save(event);

        List<Participant> studentParticipants = this.moduleService
            .findAllStudentModulesByModuleId(eventDTO.getModuleId()).stream()
            .map(sm -> Participant.builder().user(sm.getStudent()).event(event).build())
            .toList();

        List<Participant> professorParticipants = this.moduleService
            .findAllProfessorModulesByModuleId(eventDTO.getModuleId()).stream()
            .map(pm -> Participant.builder().user(pm.getProfessor()).event(event).build())
            .toList();

        List<Participant> allParticipants = Stream.concat(studentParticipants.stream(), professorParticipants.stream())
            .toList();

        this.participantRepository.saveAll(allParticipants);

        this.notificationService.sendEventCreationNotification(event, allParticipants);
    }

    public EventDTO updateEvent(int eventId, EventModificationDTO eventDTO) {
        Event event = this.findById(eventId);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Role.PROFESSOR.equals(user.getRole())
            && !this.isParticipant(user.getId(), event.getId()))
            throw new AccessDeniedException("Access denied");

        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());

        this.eventRepository.save(event);

        List<Participant> participants = this.participantRepository.findAllByEventId(eventId);

        this.notificationService.sendEventModificationNotification(user, event, participants);

        User publisher = event.getPublisher();

        FullnameDTO publisherDTO = FullnameDTO.builder()
            .id(publisher.getId())
            .fullname(publisher.getFirstname() + " " + publisher.getLastname())
            .build();

        return EventDTO.builder()
            .title(event.getTitle())
            .description(event.getDescription())
            .date(event.getDate())
            .module(event.getModule())
            .publisher(publisherDTO)
            .createdAt(event.getCreatedAt())
            .build();
    }

    public void deleteEvent(int eventId) {
        Event event = this.findById(eventId);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Role.PROFESSOR.equals(user.getRole())
            && !this.isParticipant(user.getId(), event.getId()))
            throw new AccessDeniedException("Access denied");

        event.setDeleted(true);
        this.eventRepository.save(event);

        List<Participant> participants = this.participantRepository.findAllByEventId(eventId);

        this.notificationService.sendEventDeletionNotification(user, event, participants);
    }

    public List<LiteEventDTO> getAllEvents() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Stream<Event> events;
        if (Role.PROFESSOR.equals(user.getRole()) || Role.STUDENT.equals(user.getRole()))
            events = this.participantRepository.findAllByUserId(user.getId())
                .map(Participant::getEvent);
        else
            events = this.eventRepository.findAll().stream();

        return events.map(EventMapper::toLiteEventDTO).toList();
    }

    public EventDTO getEventById(int id) {
        Event event = this.findById(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ((Role.PROFESSOR.equals(user.getRole()) || Role.STUDENT.equals(user.getRole()))
            && !this.isParticipant(user.getId(), event.getId()))
            throw new AccessDeniedException("Access denied");

        User publisher = event.getPublisher();
        FullnameDTO publisherDTO = FullnameDTO.builder()
            .id(publisher.getId())
            .fullname(publisher.getFirstname() + " " + publisher.getLastname())
            .build();

        return EventDTO.builder()
            .title(event.getTitle())
            .description(event.getDescription())
            .date(event.getDate())
            .module(event.getModule())
            .publisher(publisherDTO)
            .createdAt(event.getCreatedAt())
            .build();
    }



    private Event findById(int id) {
        return this.eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isParticipant(int userId, int eventId) {
        return this.participantRepository.existsByUserIdAndEventId(userId, eventId);
    }
}
