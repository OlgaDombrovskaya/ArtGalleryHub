package com.art_gallery_hub.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

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
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return hostUrl + uniqueFileName;
    }

    private String generateUniqueFileName(MultipartFile file) {
        String origFileName = file.getOriginalFilename();
        String randomUuid = UUID.randomUUID().toString();

        return randomUuid + "_" + origFileName;
    }
}
