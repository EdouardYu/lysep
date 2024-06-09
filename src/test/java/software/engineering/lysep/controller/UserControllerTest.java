package software.engineering.lysep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import software.engineering.lysep.security.TestSecurityConfig;
import software.engineering.lysep.dto.user.*;
import software.engineering.lysep.entity.enumeration.Role;
import software.engineering.lysep.security.JwtService;
import software.engineering.lysep.service.UserService;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtService jwtService;

    @Test
    public void testSignUp() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO(
            "Edouard",
            "Yu",
            "Azerty1234!",
            "(+33)782475788",
            Role.STUDENT
        );

        this.mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testActivate() throws Exception {
        ActivationDTO activationDTO = ActivationDTO.builder()
            .email("edouard.yu@eleve.isep.com")
            .code("888888")
            .build();

        this.mockMvc.perform(post("/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(activationDTO)))
            .andExpect(status().isOk());
    }

    @Test
    public void testNewActivationCode() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .email("edouard.yu@eleve.isep.com")
            .build();

        this.mockMvc.perform(post("/activate/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDTO)))
            .andExpect(status().isOk());
    }

    @Test
    public void testSignIn() throws Exception {
        AuthenticationDTO authenticationDTO = AuthenticationDTO.builder()
            .email("edouard.yu@eleve.isep.com")
            .password("Azerty1234!")
            .build();

        when(this.jwtService.generate(any(String.class))).thenReturn(Map.of("bearer", "token"));


        this.mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationDTO)))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.bearer").exists());
    }

    @Test
    public void testSignOut() throws Exception {
        this.mockMvc.perform(post("/signout"))
            .andExpect(status().isOk());
    }

    @Test
    public void testResetPassword() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .email("edouard.yu@eleve.isep.com")
            .build();

        this.mockMvc.perform(post("/password/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDTO)))
            .andExpect(status().isOk());
    }

    @Test
    public void testNewPassword() throws Exception {
        PasswordResetDTO passwordResetDTO = PasswordResetDTO.builder()
            .email("edouard.yu@eleve.isep.com")
            .code("888888")
            .password("Azerty1234!")
            .build();

        this.mockMvc.perform(post("/password/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordResetDTO)))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetProfile() throws Exception {
        ProfileDTO profile = ProfileDTO.builder()
            .email("edouard.yu@eleve.isep.com")
            .firstname("Edouard")
            .lastname("Yu")
            .username("Edouard Yu")
            .phone("(+33)782475788")
            .role(Role.STUDENT)
            .build();

        when(userService.getProfile(any(Integer.class))).thenReturn(profile);

        this.mockMvc.perform(get("/profiles/1"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.email").value("edouard.yu@eleve.isep.com"))
            .andExpect(jsonPath("$.firstname").value("Edouard"))
            .andExpect(jsonPath("$.lastname").value("Yu"))
            .andExpect(jsonPath("$.username").value("Edouard Yu"))
            .andExpect(jsonPath("$.phone").value("(+33)782475788"))
            .andExpect(jsonPath("$.role").value("STUDENT"));


        this.mockMvc.perform(get("/profiles/1"))
            .andExpect(status().isOk())
            .andDo(print())
        ;
    }

    @Test
    public void testModifyProfile() throws Exception {
        ProfileModificationDTO profileModificationDTO = ProfileModificationDTO.builder()
            .username("EdouardY")
            .phone("(+33)682475788")
            .build();

        ProfileDTO profile = ProfileDTO.builder()
            .email("edouard.yu@eleve.isep.com")
            .firstname("Edouard")
            .lastname("Yu")
            .username("EdouardY")
            .phone("(+33)682475788")
            .role(Role.STUDENT)
            .build();

        when(userService.modifyProfile(any(Integer.class), any(ProfileModificationDTO.class))).thenReturn(profile);

        this.mockMvc.perform(put("/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileModificationDTO)))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.email").value("edouard.yu@eleve.isep.com"))
            .andExpect(jsonPath("$.firstname").value("Edouard"))
            .andExpect(jsonPath("$.lastname").value("Yu"))
            .andExpect(jsonPath("$.username").value("EdouardY"))
            .andExpect(jsonPath("$.phone").value("(+33)682475788"))
            .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    public void testModifyPassword() throws Exception {
        PasswordModificationDTO passwordModificationDTO = PasswordModificationDTO.builder()
            .oldPassword("Azerty1234!")
            .newPassword("Password1!")
            .build();

        this.mockMvc.perform(put("/profiles/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordModificationDTO)))
            .andExpect(status().isOk());
    }
}
