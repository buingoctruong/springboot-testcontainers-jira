package github.io.truongbn.jiratestcontainers.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class LocalJiraInitializer
        implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(@NonNull ConfigurableApplicationContext context) {
        JiraLocalSetup(context);
    }

    private void JiraLocalSetup(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        DockerImageName dockerImageName = DockerImageName.parse("atlassian/jira-software");
        GenericContainer<?> jira = new GenericContainer<>(dockerImageName).withExposedPorts(8080)
                .withCreateContainerCmdModifier(cmd -> cmd.withName("jira"))
                .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig()
                        .withBinds(new com.github.dockerjava.api.model.Bind("jiraVolume",
                                new com.github.dockerjava.api.model.Volume(
                                        "/var/atlassian/application-data/jira"))))
                .withEnv("JIRA_ADMIN_USERNAME", "admin").withEnv("JIRA_ADMIN_PASSWORD", "password")
                .withEnv("HEAP_NEWSIZE", "128M").withEnv("MAX_HEAP_SIZE", "1024M");
        jira.start();
        Integer mappedPort = jira.getMappedPort(8080);
        String address = jira.getHost();
        setProperties(environment, "jira.config.jira-url", "http://" + address + ":" + mappedPort);
    }

    private void setProperties(ConfigurableEnvironment environment, String name, Object value) {
        MutablePropertySources sources = environment.getPropertySources();
        PropertySource<?> source = sources.get(name);
        if (source == null) {
            source = new MapPropertySource(name, new HashMap<>());
            sources.addFirst(source);
        }
        ((Map<String, Object>) source.getSource()).put(name, value);
    }
}
