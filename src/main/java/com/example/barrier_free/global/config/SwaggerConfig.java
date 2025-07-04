package com.example.barrier_free.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	private static final String SECURITY_SCHEME_NAME = "BearerAuth";

	@Bean
	public OpenAPI openAPI() {
		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT");

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList(SECURITY_SCHEME_NAME);

		return new OpenAPI()
			.addSecurityItem(securityRequirement)
			.components(new Components()
				.addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme)) // 등록!
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("배리어프리 어플")
			.description("배리어프리 - api 명세서")
			.version("1.0.0");
	}
}

