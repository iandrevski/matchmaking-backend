package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.service.domain.PhotoStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

// сликите се чуваат во папка на серверот; за AWS се заменува со S3 имплементација
@Service
public class FileSystemPhotoStorageService implements PhotoStorageService {

    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
    );

    private final Path venuesDir;

    public FileSystemPhotoStorageService(@Value("${app.uploads-dir}") String uploadsDir) {
        this.venuesDir = Path.of(uploadsDir, "venues").toAbsolutePath().normalize();
    }

    @Override
    public String store(MultipartFile file) {
        String extension = EXTENSIONS.get(file.getContentType());
        if (extension == null) {
            throw new InvalidOperationException("Only JPEG, PNG and WebP images are allowed");
        }
        // името го смислуваме ние, никогаш не го користиме името од корисникот
        String fileName = UUID.randomUUID() + "." + extension;
        try (InputStream input = file.getInputStream()) {
            Files.createDirectories(venuesDir);
            Files.copy(input, venuesDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not store photo", e);
        }
        return fileName;
    }

    @Override
    public void delete(String fileName) {
        try {
            Files.deleteIfExists(venuesDir.resolve(fileName));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not delete photo", e);
        }
    }
}