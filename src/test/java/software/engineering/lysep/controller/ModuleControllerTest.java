package software.engineering.lysep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import software.engineering.lysep.dto.module.AssignUserDTO;
import software.engineering.lysep.security.TestSecurityConfig;
import software.engineering.lysep.service.ModuleService;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class ModuleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ModuleService moduleService;

    @Test
    public void testAssignStudents() throws Exception {
        AssignUserDTO assignUserDTO = AssignUserDTO.builder()
            .moduleId(1)
            .userIds(Arrays.asList(1, 2, 3, 4, 5))
            .build();

        this.mockMvc.perform(post("/assign/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignUserDTO)))
            .andExpect(status().isOk());
    }

    @Test
    public void testAssignProfessors() throws Exception {
        AssignUserDTO assignUserDTO = AssignUserDTO.builder()
            .moduleId(1)
            .userIds(Arrays.asList(32, 33))
            .build();

        mockMvc.perform(post("/assign/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignUserDTO)))
            .andExpect(status().isOk());
    }
}