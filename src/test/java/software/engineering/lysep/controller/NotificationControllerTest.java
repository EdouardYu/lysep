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
import software.engineering.lysep.dto.notification.AlertDTO;
import software.engineering.lysep.dto.notification.NotificationDTO;
import software.engineering.lysep.security.JwtService;
import software.engineering.lysep.security.TestSecurityConfig;
import software.engineering.lysep.service.NotificationService;
import software.engineering.lysep.service.UserService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NotificationController.class)
@Import(TestSecurityConfig.class)
class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    NotificationService notificationService;

    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testGetUserNotifications() throws Exception {
        NotificationDTO notification1 = NotificationDTO.builder()
            .title("Notification 1")
            .content("Content 1")
            .build();

        NotificationDTO notification2 = NotificationDTO.builder()
            .title("Notification 2")
            .content("Content 2")
            .build();

        when(notificationService.getUserNotifications()).thenReturn(List.of(notification1, notification2));

        this.mockMvc.perform(get("/notifications"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].title").value("Notification 1"))
            .andExpect(jsonPath("$[0].content").value("Content 1"))
            .andExpect(jsonPath("$[1].title").value("Notification 2"))
            .andExpect(jsonPath("$[1].content").value("Content 2"))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testGetUserAlerts() throws Exception {
        AlertDTO alert1 = AlertDTO.builder()
            .title("Alert 1")
            .content("Alert Content 1")
            .build();

        AlertDTO alert2 = AlertDTO.builder()
            .title("Alert 2")
            .content("Alert Content 2")
            .build();

        when(notificationService.getUserAlerts()).thenReturn(List.of(alert1, alert2));

        this.mockMvc.perform(get("/alerts"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].title").value("Alert 1"))
            .andExpect(jsonPath("$[0].content").value("Alert Content 1"))
            .andExpect(jsonPath("$[1].title").value("Alert 2"))
            .andExpect(jsonPath("$[1].content").value("Alert Content 2"))
            .andDo(print());
    }
}
