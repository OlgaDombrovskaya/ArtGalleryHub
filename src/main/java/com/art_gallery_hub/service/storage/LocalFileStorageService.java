package com.art_gallery_hub.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    private final String uploadDirName;
    private final String hostUrl;

    public LocalFileStorageService(
            @Value("${upload.dir}") String uploadDirName,
            @Value("${host.url}") String hostUrl
    ) {
        this.uploadDirName = uploadDirName;
        this.hostUrl = hostUrl;
    }

    @Override
    public String storeFile(MultipartFile file) {
        File uploadDir = new File(uploadDirName);
        uploadDir.mkdirs();

        String uniqueFileName = generateUniqueFileName(file);
        File targetFile = new File(uploadDir, uniqueFileName);

        try (FileOutputStream out = new FileOutputStream(targetFile)) {
            out.write(file.getBytes());
            log.info("File successfully saved to {}", targetFile.getAbsolutePath());
        } catch (Exception exception) {
            log.error("Failed to save file: {}", exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
        log.info("File URL: {}", hostUrl + "/" + uniqueFileName);
        return hostUrl + uniqueFileName;
    }

    private String generateUniqueFileName(MultipartFile file) {
        String origFileName = file.getOriginalFilename();
        String randomUuid = UUID.randomUUID().toString();

        return randomUuid + "_" + origFileName;
    }
}
