package software.engineering.lysep.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.engineering.lysep.dto.module.AssignUserDTO;
import software.engineering.lysep.dto.user.FullnameDTO;
import software.engineering.lysep.entity.*;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.entity.enumeration.Role;
import software.engineering.lysep.repository.ModuleRepository;
import software.engineering.lysep.repository.ProfessorModuleRepository;
import software.engineering.lysep.repository.StudentModuleRepository;
import software.engineering.lysep.service.exception.NotFoundException;

import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final UserService userService;
    private final StudentModuleRepository studentModuleRepository;
    private final ProfessorModuleRepository professorModuleRepository;

    public void assignStudents(AssignUserDTO assignUserDTO) {
        Module module = this.findById(assignUserDTO.getModuleId());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(Role.PROFESSOR.equals(user.getRole())
            && !this.isProfessorAssignedToModule(user.getId(), module.getId()))
            throw new AccessDeniedException("Access denied");

        List<User> students = this.userService.findAllByIdInAndRole(assignUserDTO.getUserIds(), Role.STUDENT);

        // Retrieve current students from the module
        List<User> currentStudents = this.findAllStudentModulesByModuleId(module.getId()).stream()
            .map(StudentModule::getStudent).toList();

        // Determine which students to add and remove
        List<User> studentsToAdd = students.stream().filter(student -> !currentStudents.contains(student)).toList();
        List<User> studentsToRemove = currentStudents.stream().filter(student -> !students.contains(student)).toList();

        // Add new students
        List<StudentModule> studentModulesToAdd = studentsToAdd.stream()
            .map(student -> StudentModule.builder()
                .module(module)
                .student(student)
                .build()
            ).toList();

        this.studentModuleRepository.saveAll(studentModulesToAdd);

        // Remove unwanted students
        this.studentModuleRepository.deleteByModuleIdAndStudentIdIn(
            module.getId(),
            studentsToRemove.stream().map(User::getId).toList()
        );
    }

    public void assignProfessors(AssignUserDTO assignUserDTO) {
        Module module = this.findById(assignUserDTO.getModuleId());

        List<User> professors = this.userService.findAllByIdInAndRole(assignUserDTO.getUserIds(), Role.PROFESSOR);

        // Retrieve current professors from the module
        List<User> currentProfessors = this.findAllProfessorModulesByModuleId(module.getId()).stream()
            .map(ProfessorModule::getProfessor).toList();

        // Determine which professors to add and remove
        List<User> professorsToAdd = professors.stream().filter(professor -> !currentProfessors.contains(professor)).toList();
        List<User> professorsToRemove = currentProfessors.stream().filter(professor -> !professors.contains(professor)).toList();

        // Add new professors
        List<ProfessorModule> professorModulesToAdd = professorsToAdd.stream()
            .map(professor -> ProfessorModule.builder()
                .module(module)
                .professor(professor)
                .build()
            ).toList();

        this.professorModuleRepository.saveAll(professorModulesToAdd);

        // Remove unwanted professors
        this.professorModuleRepository.deleteByModuleIdAndProfessorIdIn(
            module.getId(),
            professorsToRemove.stream().map(User::getId).toList()
        );
    }

    public List<FullnameDTO> findStudentsByModuleId(int moduleId) {
        return this.findAllStudentModulesByModuleId(moduleId).stream()
            .map(sm -> FullnameDTO.builder()
                .id(sm.getStudent().getId())
                .fullname(sm.getStudent().getFirstname() + " " + sm.getStudent().getLastname())
                .build()
            ).toList();
    }

    public List<FullnameDTO> findProfessorsByModuleId(int moduleId) {
        return this.findAllProfessorModulesByModuleId(moduleId).stream()
            .map(pm -> FullnameDTO.builder()
                .id(pm.getProfessor().getId())
                .fullname(pm.getProfessor().getFirstname() + " " + pm.getProfessor().getLastname())
                .build()
            ).toList();
    }

    public List<Module> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(Role.PROFESSOR.equals(user.getRole()))
            return this.findAllProfessorModulesByProfessorId(user.getId()).stream()
                .map(ProfessorModule::getModule)
                .toList();

        if(Role.STUDENT.equals(user.getRole()))
            return this.findAllStudentModulesByStudentId(user.getId()).stream()
                .map(StudentModule::getModule)
                .toList();

        return this.moduleRepository.findAll();
    }

    public Module findById(int id) {
        return this.moduleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Module not found"));
    }

    public List<StudentModule> findAllStudentModulesByModuleId(int id) {
        return this.studentModuleRepository.findAllByModuleId(id);
    }

    public List<ProfessorModule> findAllProfessorModulesByModuleId(int id) {
        return this.professorModuleRepository.findAllByModuleId(id);
    }

    public List<StudentModule> findAllStudentModulesByStudentId(int id) {
        return this.studentModuleRepository.findAllByStudentId(id);
    }

    public List<ProfessorModule> findAllProfessorModulesByProfessorId(int id) {
        return this.professorModuleRepository.findAllByProfessorId(id);
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isProfessorAssignedToModule(int professorId, int moduleId) {
        return this.professorModuleRepository.existsByProfessorIdAndModuleId(professorId, moduleId);
    }
}
