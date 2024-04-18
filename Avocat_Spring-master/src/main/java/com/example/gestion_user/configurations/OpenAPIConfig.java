package com.example.gestion_user.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(infoAPI())
                .components(new Components().addSchemas("MultipartFile", new MultipartFileSchema()));
    }

    //crée et configure un objet Info qui contient des informations sur votre API, telles que le titre, la description et les détails de contact
    public Info infoAPI() {
        return new Info().title("PI -MOBILITE INTERNATIONALE")
                .description("TP étude de cas")
                .contact(contactAPI());
    }
    // crée et configure un objet Contact qui représente les informations de contact pour l'équipe responsable de l'API
    public Contact contactAPI() {
        Contact contact = new Contact().name("Equipe ASI II")
                .email("***************************@gmail.com")
                .url("https://www.linkedin.com/in/**********/");
        return contact;
    }

    private static class MultipartFileSchema extends ByteArraySchema {
        public MultipartFileSchema() {
            super();
            this.format("binary");
        }
    }

}


