package com.semo.group1.on_dongnae.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "BearerAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);
        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(new Info()
                        .title("온-동네 API 명세서")
                        .description("온-동네 백엔드 API 서버의 명세서입니다.")
                        .version("v1.0.0"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("https://api.on-dongnae.site").description("Production Server"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("http://localhost:8080").description("Local Server"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
