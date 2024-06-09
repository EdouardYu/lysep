package software.engineering.lysep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.engineering.lysep.entity.id.StudentModuleId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_module")
@IdClass(StudentModuleId.class)
public class StudentModule {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    @Id
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
}