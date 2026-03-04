package com.firefly.domain.common.notifications.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Spring Boot entry point for the Domain Common Notifications service.
 * <p>
 * Bootstraps the application with WebFlux, CQRS command/query processing,
 * and OpenAPI documentation generation.
 * </p>
 */
@SpringBootApplication(scanBasePackages = {
        "com.firefly.domain.common.notifications",
        "org.fireflyframework.web"
})
@EnableWebFlux
@ConfigurationPropertiesScan
@OpenAPIDefinition(
        info = @Info(
                title = "Domain Common Notifications",
                version = "${spring.application.version}",
                description = "Domain layer service for multi-channel notification orchestration",
                contact = @Contact(
                        name = "${spring.application.team.name}",
                        email = "${spring.application.team.email}"
                )
        ),
        servers = {
                @Server(url = "http://core.getfirefly.io/domain-common-notifications", description = "Development Environment"),
                @Server(url = "/", description = "Local Development Environment")
        }
)
public class DomainCommonNotificationsApplication {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DomainCommonNotificationsApplication.class, args);
    }
}
