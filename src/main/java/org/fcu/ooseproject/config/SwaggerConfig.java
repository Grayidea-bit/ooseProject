package org.fcu.ooseproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Issue Tracking System API")
                        .description("A RESTful API for managing daily tasks and scheduled issues")
                        .version("1.0")
                        .contact(new Contact()
                                .name("FCU OOSE Project")
                                .email("example@fcu.edu.tw")
                                .url("https://github.com/yourusername/ooseProject"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
} 