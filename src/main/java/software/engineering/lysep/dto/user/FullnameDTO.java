package software.engineering.lysep.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FullnameDTO {
    private int id;
    private String fullname;
}
