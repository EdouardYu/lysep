package software.engineering.lysep.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.engineering.lysep.dto.module.AssignUserDTO;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.entity.User;
import software.engineering.lysep.service.ModuleService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("modules")
public class ModuleController {
    private final ModuleService moduleService;

    @PreAuthorize("hasAnyRole('PROFESSOR', 'SCHOOL_ADMINISTRATOR', 'APPLICATION_ADMINISTRATOR')")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "assign/students", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignStudents(@RequestBody AssignUserDTO assignUserDTO) {
        this.moduleService.assignStudents(assignUserDTO);
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'SCHOOL_ADMINISTRATOR', 'APPLICATION_ADMINISTRATOR')")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "assign/professors", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignProfessors(@RequestBody AssignUserDTO assignUserDTO) {
        this.moduleService.assignProfessors(assignUserDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "{moduleId}/students", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getStudentsByModuleId(@PathVariable int moduleId) {
        return this.moduleService.findStudentsByModuleId(moduleId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "{moduleId}/professors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getProfessorsByModuleId(@PathVariable int moduleId) {
        return this.moduleService.findProfessorsByModuleId(moduleId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Module> getAllModules() {
        return this.moduleService.findAll();
    }
}
