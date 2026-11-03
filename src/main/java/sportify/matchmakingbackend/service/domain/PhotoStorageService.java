package sportify.matchmakingbackend.service.domain;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {

    // ја зачувува сликата и го враќа новото име на фајлот
    String store(MultipartFile file);

    void delete(String fileName);
}