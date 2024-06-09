package software.engineering.lysep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.engineering.lysep.entity.id.ProfessorModuleId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "professor_module")
@IdClass(ProfessorModuleId.class)
public class ProfessorModule {
    @Id
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private User professor;
    @Id
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
}