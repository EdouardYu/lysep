package software.engineering.lysep.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.engineering.lysep.dto.user.*;
import software.engineering.lysep.entity.User;
import software.engineering.lysep.entity.Validation;
import software.engineering.lysep.repository.UserRepository;
import software.engineering.lysep.service.exception.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    public void signUp(RegistrationDTO userDTO) {
        if(this.userRepository.existsByEmailAndEnabled(userDTO.getEmail(), true))
            throw new AlreadyUsedException("You are already registered");

        Optional<User> dbUser = this.userRepository.findByEmail(userDTO.getEmail());
        String encryptedPassword = this.passwordEncoder.encode(userDTO.getPassword());

        User user;
        if(dbUser.isPresent()){
            user = dbUser.get();
            user.setPassword(encryptedPassword);
            user.setPhone(userDTO.getPhone());
        } else {
            user = User.builder()
                .email(userDTO.getEmail())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .username(userDTO.getUsername())
                .password(encryptedPassword)
                .enabled(false)
                .phone(userDTO.getPhone())
                .role(userDTO.getRole())
                .build();
        }

        this.userRepository.save(user);

        this.validationService.register(user);
    }

    public void activate(ActivationDTO activationDTO) {
        Validation validation = this.validationService.findUserActivationCode(
            activationDTO.getEmail(),
            activationDTO.getCode()
        );

        if(Instant.now().isAfter(validation.getExpiredAt()))
            throw new ValidationCodeException("Expired activation code");

        if(!validation.isEnabled())
            throw new ValidationCodeException("Disabled activation code");

        User user = validation.getUser();
        if(user.isEnabled())
            throw new AlreadyProcessedException("User already enabled");

        user.setEnabled(true);
        this.userRepository.save(user);
    }

    public void newActivationCode(EmailDTO userDTO) {
        User user = this.userRepository.findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(user.isEnabled())
            throw new AlreadyProcessedException("User already enabled");

        this.validationService.register(user);
    }

    public void resetPassword(EmailDTO userDTO) {
        User user = this.userRepository.findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!user.isEnabled())
            throw new NotYetEnabledException("User not yet enabled");

        this.validationService.resetPassword(user);
    }

    public void newPassword(PasswordResetDTO passwordResetDTO) {
        Validation validation = validationService.findUserPasswordResetCode(
            passwordResetDTO.getEmail(),
            passwordResetDTO.getCode()
        );

        User user = validation.getUser();
        if(!user.isEnabled())
            throw new NotYetEnabledException("User not yet enabled");

        if(Instant.now().isAfter(validation.getExpiredAt()))
            throw new ValidationCodeException("Expired password reset code");

        if(!validation.isEnabled())
            throw new ValidationCodeException("Disabled password reset code");

        String encryptedPassword = this.passwordEncoder.encode(passwordResetDTO.getPassword());
        user.setPassword(encryptedPassword);
        this.userRepository.save(user);
    }

    public ProfileDTO getProfile(int id) {
        User profile = this.userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ProfileDTO.builder()
            .email(profile.getEmail())
            .firstname(profile.getFirstname())
            .lastname(profile.getLastname())
            .username(profile.getUsername())
            .phone(profile.getPhone())
            .role(profile.getRole())
            .build();
    }


    public ProfileDTO modifyProfile(int id, ProfileModificationDTO userDTO) {
        User user = hasPermission(id);

        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());

        user = this.userRepository.save(user);

        return ProfileDTO.builder()
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .username(user.getUsername())
            .phone(user.getPhone())
            .role(user.getRole())
            .build();
    }

    public void modifyPassword(int id, PasswordModificationDTO userDTO) {
        User user = hasPermission(id);

        if (!passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword())) {
            throw new BadPasswordException("Incorrect password");
        }

        String newEncryptedPassword = this.passwordEncoder.encode(userDTO.getNewPassword());

        user.setPassword(newEncryptedPassword);
        this.userRepository.save(user);
    }

    private User hasPermission(int id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbUser = this.userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(user.getId() != dbUser.getId() && !user.getRole().name().contains("ADMINISTRATOR"))
            throw new AccessDeniedException("Access denied");

        return dbUser;
    }

    @Override
    public User loadUserByUsername(String username) {
        return this.userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> finAllById(List<Integer> ids) {
        // Avoid duplicates
        Set<Integer> set = new HashSet<>(ids);
        ids = new ArrayList<>(set);

        List<User> users = this.userRepository.findAllById(ids);
        Set<Integer> foundUserIds = users.stream().map(User::getId).collect(Collectors.toSet());

        if (foundUserIds.size() != ids.size()) {
            List<Integer> missingIds = ids.stream()
                .filter(id -> !foundUserIds.contains(id))
                .toList();

            throw new NotFoundException("Students not found for IDs: " + missingIds);
        }

        return users;
    }
}
