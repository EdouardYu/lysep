package software.engineering.lysep.dto.module;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class AssignUserDTO {
    @JsonProperty("user_ids")
    private List<Integer> userIds;
    @JsonProperty("module_id")
    private int moduleId;
}
