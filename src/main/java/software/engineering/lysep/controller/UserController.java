package software.engineering.lysep.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import software.engineering.lysep.dto.user.*;
import software.engineering.lysep.security.JwtService;
import software.engineering.lysep.service.UserService;

import java.util.Map;

@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void signUp(@Valid @RequestBody RegistrationDTO userDTO) {
        this.userService.signUp(userDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "activate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void activate(@RequestBody ActivationDTO activationDTO) {
        this.userService.activate(activationDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "activate/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void newActivationCode(@RequestBody EmailDTO userDTO) {
        this.userService.newActivationCode(userDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(
        path = "signin",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> signIn(@RequestBody AuthenticationDTO authenticationDTO) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            authenticationDTO.getEmail(),
            authenticationDTO.getPassword()
        ));

        return this.jwtService.generate(authenticationDTO.getEmail());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "signout")
    public void signOut() {
        this.jwtService.signOut();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "password/reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void resetPassword(@RequestBody EmailDTO userDTO) {
        this.userService.resetPassword(userDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "password/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void newPassword(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
        this.userService.newPassword(passwordResetDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "profiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO getProfile(@PathVariable int id) {
        return this.userService.getProfile(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(
        path = "profiles/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ProfileDTO modifyProfile(@PathVariable int id, @Valid @RequestBody ProfileModificationDTO userDTO) {
        return this.userService.modifyProfile(id, userDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "profiles/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void modifyPassword(@PathVariable int id, @Valid @RequestBody PasswordModificationDTO userDTO) {
        this.userService.modifyPassword(id, userDTO);
    }
}

