package com.example.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Mapea a todos los endpoints
                .allowedOrigins("*")  // Permitir solicitudes de cualquier origen
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Permitir los métodos HTTP especificados
                .allowedHeaders("*");  // Permitir todos los encabezados
    }
}
