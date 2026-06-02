package com.hammadyaqoob.spring_ai_subagents;

import com.hammadyaqoob.spring_ai_subagents.config.OrchestratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SpringAiSubagentsApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringAiSubagentsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringAiSubagentsApplication.class, args);
	}

	@Bean
	@Profile("!test")
    CommandLineRunner demo(OrchestratorService orchestratorService) {
		return args -> {
			String response = orchestratorService.ask(
					"Perform the following tasks on the directory C:\\Users\\hamma\\IdeaProjects\\spring-ai-subagents and also state which agents are being used for each tasks:\n"
							+ "- Review the code quality of a current Java Spring Boot application\n"
							+ "- Generate concise technical documentation like user guide"
			);
			logger.info("{}", response);
		};
	}
}
