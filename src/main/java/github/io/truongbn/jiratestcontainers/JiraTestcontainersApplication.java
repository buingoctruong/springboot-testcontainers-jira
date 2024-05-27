package github.io.truongbn.jiratestcontainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class JiraTestcontainersApplication {
    public static void main(String[] args) {
        SpringApplication.run(JiraTestcontainersApplication.class, args);
    }
}
