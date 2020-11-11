package it.univaq.disim.mwt.j2etpapp.utils;

import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileDealer {

    @Autowired
    private ApplicationProperties properties;

    public String uploadFile(MultipartFile image) throws IOException {
        String randomUUID = UUID.randomUUID().toString();
        String filename = randomUUID + '.' + FilenameUtils.getExtension(image.getOriginalFilename());
        String absolutePath = properties.getImagesStoragePathAbsolute() + filename;

        File file = new File(absolutePath);
        FileUtils.touch(file);

        FileUtils.writeByteArrayToFile(file, image.getBytes());

        return filename;
    }

    public void removeFile(String location) {
        Path relativePath = Paths.get(location);
        String absolutePath = properties.getImagesStoragePathAbsolute() + relativePath.getFileName().toString();
        File file = new File(absolutePath);
        FileUtils.deleteQuietly(file);
    }
}
