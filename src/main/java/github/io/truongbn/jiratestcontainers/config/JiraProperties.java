package github.io.truongbn.jiratestcontainers.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@ConfigurationProperties(prefix = "jira.config")
public class JiraProperties {
    private final String jiraUrl;
    private final String username;
    private final String password;
}
