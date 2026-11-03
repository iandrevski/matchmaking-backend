package sportify.matchmakingbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

// GET /uploads/venues/<file>.jpg ја враќа сликата од папката на серверот
@Configuration
public class UploadsConfig implements WebMvcConfigurer {

    private final String uploadsLocation;

    public UploadsConfig(@Value("${app.uploads-dir}") String uploadsDir) {
        this.uploadsLocation = Path.of(uploadsDir).toAbsolutePath().normalize().toUri() + "/";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations(uploadsLocation);
    }
}