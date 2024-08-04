package com.bokyoung.moai.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("!test")
@OpenAPIDefinition(
        security = {
                @SecurityRequirement(name = "X-Auth-Token"),
        }
)
@SecuritySchemes({
        @SecurityScheme(name = "X-Auth-Token",
                type = SecuritySchemeType.APIKEY,
                description = "Api token",
                in = SecuritySchemeIn.HEADER,
                paramName = "X-Auth-Token"),
})
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI moaiAPI() {
        Info info = new Info()
            .title("MOAI-API")
            .version("3.0")
            .description("MOAI 개발용 API 모음");

        return new OpenAPI()
            .addServersItem(new Server().url("/"))
            .components(new Components())
            .info(info);
    }
}