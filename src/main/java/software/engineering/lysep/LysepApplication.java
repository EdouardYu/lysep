package software.engineering.lysep;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@AllArgsConstructor
@EnableScheduling
@SpringBootApplication
public class LysepApplication {
    public static void main(String[] args) {
        SpringApplication.run(LysepApplication.class, args);
    }
}
