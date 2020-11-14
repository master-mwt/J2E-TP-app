package it.univaq.disim.mwt.j2etpapp.utils;

import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class FileDealer {

    @Autowired
    private ApplicationProperties properties;

    public String uploadFile(MultipartFile image) throws IOException {
        String randomUUID = UUID.randomUUID().toString();
        String filename = randomUUID + '.' + FilenameUtils.getExtension(image.getOriginalFilename());
        String path = properties.getImagesStoragePath() + filename;

        File file = new File(path);
        FileUtils.touch(file);

        FileUtils.writeByteArrayToFile(file, image.getBytes());

        log.info("Uploaded file: " + path);
        return path;
    }

    public void removeFile(String location) {
        File file = new File(location);
        FileUtils.deleteQuietly(file);
        log.info("Deleted file: " + location);
    }
}
