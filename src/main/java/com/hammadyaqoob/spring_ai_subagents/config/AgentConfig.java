package com.hammadyaqoob.spring_ai_subagents.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springaicommunity.agent.common.task.subagent.SubagentReference;
import org.springaicommunity.agent.common.task.subagent.SubagentType;
import org.springaicommunity.agent.tools.task.TaskTool;
import org.springaicommunity.agent.tools.task.claude.ClaudeSubagentType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class AgentConfig {
    @Value("${agent.tasks.paths:classpath*:agents/*.md}")
    private String agentTasksPath;

    private final ResourcePatternResolver resourcePatternResolver;

    public AgentConfig(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @Bean
    @Primary
    public ChatClient orchestratorChatClient(ChatClient.Builder chatClientBuilder) {
        List<SubagentReference> agentReferences = resolveAgentReferences();

        SubagentType claudeType = ClaudeSubagentType.builder()
                .chatClientBuilder("default", chatClientBuilder.clone())
                .build();

        ToolCallback taskTool = TaskTool.builder()
                .subagentReferences(agentReferences)
                .subagentTypes(claudeType)
                .build();

        return chatClientBuilder.clone().defaultTools(toolSpec -> toolSpec.callbacks(taskTool))
                .build();
    }

    private List<SubagentReference> resolveAgentReferences() {
        try {
            List<SubagentReference> list = Arrays.stream(resourcePatternResolver.getResources(agentTasksPath))
                    .map(this::toSubagentReference)
                    .toList();
            return list;
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to load agent definitions from pattern: " + agentTasksPath, ex);
        }
    }

    private SubagentReference toSubagentReference(Resource resource) {
        try {
            return new SubagentReference(resource.getURI().toString(), "CLAUDE", null);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to resolve agent definition URI for resource: " + resource, ex);
        }
    }
}
