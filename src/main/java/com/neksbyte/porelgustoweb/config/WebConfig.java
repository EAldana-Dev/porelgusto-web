package com.neksbyte.porelgustoweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/** Interceptor de acceso y carpeta de imágenes subidas (/uploads/**). */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AccesoInterceptor acceso;
    private final TiendaConfig tienda;

    public WebConfig(AccesoInterceptor acceso, TiendaConfig tienda) {
        this.acceso = acceso;
        this.tienda = tienda;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(acceso).excludePathPatterns("/css/**", "/js/**", "/img/**", "/imagenes/**", "/webjars/**", "/uploads/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String carpeta = Path.of(tienda.getUploadsDir()).toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/**").addResourceLocations(carpeta);
    }
}
