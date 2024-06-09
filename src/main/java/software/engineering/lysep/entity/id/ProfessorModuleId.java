package software.engineering.lysep.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.engineering.lysep.entity.Module;
import software.engineering.lysep.entity.User;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorModuleId implements Serializable {
    private User professor;
    private Module module;
}