package github.io.truongbn.jiratestcontainers;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import github.io.truongbn.jiratestcontainers.config.LocalJiraInitializer;

@SpringBootTest
@EnableConfigurationProperties
@ComponentScan(basePackages = "github.io.truongbn.jiratestcontainers")
@ConfigurationPropertiesScan(basePackages = "github.io.truongbn.jiratestcontainers")
public class JiraAppRunner {
    public static void main(String[] args) {
        new SpringApplicationBuilder(JiraAppRunner.class).initializers(new LocalJiraInitializer())
                .run(args);
    }
}
