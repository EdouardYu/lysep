package software.engineering.lysep.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.engineering.lysep.dto.module.AssignUserDTO;
import software.engineering.lysep.entity.*;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.repository.ModuleRepository;
import software.engineering.lysep.repository.ProfessorModuleRepository;
import software.engineering.lysep.repository.StudentModuleRepository;
import software.engineering.lysep.service.exception.NotFoundException;

import java.util.List;


@AllArgsConstructor
@Service
public class ModuleService {
    private ModuleRepository moduleRepository;
    private UserService userService;
    private final StudentModuleRepository studentModuleRepository;
    private final ProfessorModuleRepository professorModuleRepository;

    public void assignStudents(AssignUserDTO assignUserDTO) {
        Module module = this.moduleRepository.findById(assignUserDTO.getModuleId())
            .orElseThrow(() -> new NotFoundException("Module not found"));

        List<User> students = this.userService.finAllById(assignUserDTO.getUserIds());

        List<StudentModule> studentModules = students.stream()
            .map(student -> StudentModule.builder()
                .module(module)
                .student(student)
                .build()
            ).toList();

        this.studentModuleRepository.saveAll(studentModules);
    }

    public void assignProfessors(AssignUserDTO assignUserDTO) {
        Module module = this.moduleRepository.findById(assignUserDTO.getModuleId())
            .orElseThrow(() -> new NotFoundException("Module not found"));

        List<User> professors = this.userService.finAllById(assignUserDTO.getUserIds());

        List<ProfessorModule> professorModules = professors.stream()
            .map(professor -> ProfessorModule.builder()
                .module(module)
                .professor(professor)
                .build()
            ).toList();

        this.professorModuleRepository.saveAll(professorModules);
    }
}
