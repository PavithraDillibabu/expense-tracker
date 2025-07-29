package com.pd.expense_tracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI expenseTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Expense Tracker API")
                        .description("API documentation for Expense Tracker")
                        .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("Category APIs")
                .pathsToMatch("/api/category/**")
                .build();
    }

    @Bean
    public GroupedOpenApi expenseApi() {
        return GroupedOpenApi.builder()
                .group("Expense APIs")
                .pathsToMatch("/api/expense/**")
                .build();
    }

    @Bean
    public GroupedOpenApi analytics() {
        return GroupedOpenApi.builder()
                .group("Analytics")
                .pathsToMatch("/api/analytics/**")
                .build();
    }

    @Bean
    public GroupedOpenApi split() {
        return GroupedOpenApi.builder()
                .group("SplitExpense")
                .pathsToMatch("/api/split/**")
                .build();
    }

}
