package sportify.matchmakingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatchmakingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchmakingBackendApplication.class, args);
    }

}
