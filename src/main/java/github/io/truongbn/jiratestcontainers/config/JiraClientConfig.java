package github.io.truongbn.jiratestcontainers.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

@Configuration
public class JiraClientConfig {
    @Bean
    public IssueRestClient issueRestClient(JiraProperties jiraProperties) {
        JiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();
        return jiraRestClientFactory
                .createWithBasicHttpAuthentication(URI.create(jiraProperties.getJiraUrl()),
                        jiraProperties.getUsername(), jiraProperties.getPassword())
                .getIssueClient();
    }
}
