package software.engineering.lysep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import software.engineering.lysep.dto.event.EventCreationDTO;
import software.engineering.lysep.dto.event.EventDTO;
import software.engineering.lysep.dto.event.EventModificationDTO;
import software.engineering.lysep.dto.event.LiteEventDTO;
import software.engineering.lysep.dto.user.FullnameDTO;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.security.JwtService;
import software.engineering.lysep.security.TestSecurityConfig;
import software.engineering.lysep.service.EventService;
import software.engineering.lysep.service.UserService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventController.class)
@Import(TestSecurityConfig.class)
class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventService eventService;

    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testCreateEvent() throws Exception {
        EventCreationDTO eventCreationDTO = EventCreationDTO.builder()
            .title("New Event")
            .description("Event Description")
            .date(Instant.now().plus(1, ChronoUnit.DAYS))
            .moduleId(1)
            .build();

        this.mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCreationDTO)))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testUpdateEvent() throws Exception {
        Instant date = Instant.now().plus(2, ChronoUnit.DAYS);

        EventModificationDTO eventModificationDTO = EventModificationDTO.builder()
            .description("Updated Description")
            .date(date)
            .build();

        EventDTO eventDTO = EventDTO.builder()
            .title("Updated Event")
            .description("Updated Description")
            .date(date)
            .module(Module.builder()
                .id(1)
                .label("Module 1")
                .build())
            .build();

        when(eventService.updateEvent(anyInt(), any(EventModificationDTO.class))).thenReturn(eventDTO);

        this.mockMvc.perform(put("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventModificationDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Updated Event"))
            .andExpect(jsonPath("$.description").value("Updated Description"))
            .andExpect(jsonPath("$.date").value(date.toString()))
            .andExpect(jsonPath("$.module.label").value("Module 1"))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testDeleteEvent() throws Exception {
        this.mockMvc.perform(delete("/events/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testGetAllEvents() throws Exception {
        LiteEventDTO event1 = LiteEventDTO.builder()
            .id(1)
            .title("Event 1")
            .date(Instant.now().plus(1, ChronoUnit.DAYS))
            .module(Module.builder()
                .id(1)
                .label("Module 1")
                .build())
            .build();

        LiteEventDTO event2 = LiteEventDTO.builder()
            .id(2)
            .title("Event 2")
            .date(Instant.now().plus(2, ChronoUnit.DAYS))
            .module(Module.builder()
                .id(2)
                .label("Module 2")
                .build())
            .build();

        when(eventService.getAllEvents()).thenReturn(List.of(event1, event2));

        this.mockMvc.perform(get("/events"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].title").value("Event 1"))
            .andExpect(jsonPath("$[0].module.label").value("Module 1"))
            .andExpect(jsonPath("$[1].title").value("Event 2"))
            .andExpect(jsonPath("$[1].module.label").value("Module 2"))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testGetEventById() throws Exception {
        Instant now = Instant.now();

        EventDTO eventDTO = EventDTO.builder()
            .title("Event 1")
            .description("Event Description")
            .date(now.plus(1, ChronoUnit.DAYS))
            .publisher(FullnameDTO.builder()
                .id(1)
                .fullname("Admin Lysep")
                .build())
            .createdAt(now)
            .build();

        when(eventService.getEventById(anyInt())).thenReturn(eventDTO);

        this.mockMvc.perform(get("/events/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Event 1"))
            .andExpect(jsonPath("$.description").value("Event Description"))
            .andExpect(jsonPath("$.date").value(now.plus(1, ChronoUnit.DAYS).toString()))
            .andExpect(jsonPath("$.publisher.fullname").value("Admin Lysep"))
            .andExpect(jsonPath("$.created_at").value(now.toString()))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testHasPermission() throws Exception {
        when(eventService.hasPermission(anyInt())).thenReturn(true);

        this.mockMvc.perform(get("/events/1/permission"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"))
            .andDo(print());
    }
}
