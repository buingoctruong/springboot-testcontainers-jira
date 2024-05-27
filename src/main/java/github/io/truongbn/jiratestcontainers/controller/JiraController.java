package github.io.truongbn.jiratestcontainers.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.google.common.collect.ImmutableList;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
public class JiraController {
    private final IssueRestClient issueRestClient;
    @PostMapping(path = "/issue")
    public BasicIssue createJiraIssue(@RequestBody IssueInputDTO issueRequest) {
        IssueTypeDTO issueTypeDTO = issueRequest.getIssueType();
        IssueType issueType = new IssueType(URI.create(issueTypeDTO.getUri()), issueTypeDTO.getId(),
                issueTypeDTO.getName(), issueTypeDTO.isSubTask, issueTypeDTO.getDescription(),
                URI.create(issueTypeDTO.getIcon()));
        IssueInputBuilder issueInputBuilder = new IssueInputBuilder()
                .setProjectKey(issueRequest.getProjectKey()).setSummary(issueRequest.getSummary())
                .setIssueType(issueType).setReporterName(issueRequest.getReporterName())
                .setDescription(issueRequest.getDescription())
                .setFieldValue("labels", issueRequest.getFieldValues().get("labels"));
        IssueInput issueInput = issueInputBuilder
                .setComponentsNames(issueRequest.getComponentsNames()).build();
        BasicIssue issue;
        try {
            issue = issueRestClient.createIssue(issueInput).claim();
        } catch (RuntimeException e) {
            log.error("Failed to create issue {}", issueInput, e);
            log.info("Start creating issue without components");
            issueInput = issueInputBuilder.setComponentsNames(ImmutableList.of()).build();
            issue = issueRestClient.createIssue(issueInput).claim();
        }
        return issue;
    }
    @Data
    public static class IssueInputDTO {
        private final String projectKey;
        private final String summary;
        private final IssueTypeDTO issueType;
        private final String reporterName;
        private final String description;
        private final Map<String, List<String>> fieldValues;
        private final List<String> componentsNames;
    }
    @Data
    public static class IssueTypeDTO {
        private final String uri;
        private final long id;
        private final String name;
        private final boolean isSubTask;
        private final String description;
        private final String icon;
    }
}
