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
import software.engineering.lysep.dto.module.AssignUserDTO;
import software.engineering.lysep.dto.user.FullnameDTO;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.security.JwtService;
import software.engineering.lysep.security.TestSecurityConfig;
import software.engineering.lysep.service.ModuleService;
import software.engineering.lysep.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ModuleController.class)
@Import(TestSecurityConfig.class)
class ModuleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ModuleService moduleService;

    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testAssignStudents() throws Exception {
        AssignUserDTO assignUserDTO = AssignUserDTO.builder()
            .moduleId(1)
            .userIds(Arrays.asList(1, 2, 3, 4, 5))
            .build();

        this.mockMvc.perform(put("/modules/assign/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignUserDTO)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testAssignProfessors() throws Exception {
        AssignUserDTO assignUserDTO = AssignUserDTO.builder()
            .moduleId(1)
            .userIds(Arrays.asList(32, 33))
            .build();

        this.mockMvc.perform(put("/modules/assign/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignUserDTO)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testGetStudentsByModuleId() throws Exception {
        FullnameDTO student1 = FullnameDTO.builder()
            .id(1)
            .fullname("Edouard Yu")
            .build();

        FullnameDTO student2 = FullnameDTO.builder()
            .id(2)
            .fullname("Charles Radolanirina")
            .build();

        FullnameDTO student3 = FullnameDTO.builder()
            .id(3)
            .fullname("Giulietta Pancari-Fauret")
            .build();

        FullnameDTO student4 = FullnameDTO.builder()
            .id(4)
            .fullname("Prasanaa Vingadassamy")
            .build();

        FullnameDTO student5 = FullnameDTO.builder()
            .id(5)
            .fullname("Valentyna Pronina")
            .build();

        when(moduleService.findStudentsByModuleId(anyInt())).thenReturn(List.of(student1, student2, student3, student4, student5));

        this.mockMvc.perform(get("/modules/1/students"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].fullname").value("Edouard Yu"))
            .andExpect(jsonPath("$[1].fullname").value("Charles Radolanirina"))
            .andExpect(jsonPath("$[2].fullname").value("Giulietta Pancari-Fauret"))
            .andExpect(jsonPath("$[3].fullname").value("Prasanaa Vingadassamy"))
            .andExpect(jsonPath("$[4].fullname").value("Valentyna Pronina"))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testGetProfessorsByModuleId() throws Exception {
        FullnameDTO professor1 = FullnameDTO.builder()
            .id(11)
            .fullname("Zakia Kazi")
            .build();

        FullnameDTO professor2 = FullnameDTO.builder()
            .id(12)
            .fullname("Sébastien Bortenlanger")
            .build();

        when(moduleService.findProfessorsByModuleId(anyInt())).thenReturn(List.of(professor1, professor2));

        this.mockMvc.perform(get("/modules/1/professors"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].fullname").value("Zakia Kazi"))
            .andExpect(jsonPath("$[1].fullname").value("Sébastien Bortenlanger"))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"APPLICATION_ADMINISTRATOR"})
    public void testGetAllModules() throws Exception {
        Module module1 = Module.builder()
            .id(1)
            .label("Software Engineering")
            .build();

        Module module2 = Module.builder()
            .id(2)
            .label("Web Technologies")
            .build();

        when(moduleService.findAll()).thenReturn(List.of(module1, module2));

        this.mockMvc.perform(get("/modules"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].label").value("Software Engineering"))
            .andExpect(jsonPath("$[1].label").value("Web Technologies"))
            .andDo(print());
    }
}