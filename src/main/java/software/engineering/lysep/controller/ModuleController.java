package software.engineering.lysep.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.engineering.lysep.dto.module.AssignUserDTO;
import software.engineering.lysep.service.ModuleService;

@AllArgsConstructor
@RestController
@RequestMapping("modules")
public class ModuleController {
    private final ModuleService moduleService;

    @PreAuthorize("hasAnyRole('PROFESSOR', 'SCHOOL_ADMINISTRATOR', 'APPLICATION_ADMINISTRATOR')")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "assign/students", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignStudents(@RequestBody AssignUserDTO assignUserDTO) {
        this.moduleService.assignStudents(assignUserDTO);
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'SCHOOL_ADMINISTRATOR', 'APPLICATION_ADMINISTRATOR')")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "assign/professors", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignProfessors(@RequestBody AssignUserDTO assignUserDTO) {
        this.moduleService.assignProfessors(assignUserDTO);
    }
}
