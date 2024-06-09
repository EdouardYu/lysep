package software.engineering.lysep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "validation")
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    @Column(name = "expired_at")
    private Instant expiredAt;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
