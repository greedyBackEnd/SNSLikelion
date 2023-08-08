package com.example.likelionSNS.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class FileUploadUtils {
    // 이미지 저장
    public static String saveFile(String uploadDir, String originalFileName, MultipartFile multipartFile) throws IOException {
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String baseName = StringUtils.getFilename(originalFileName);
        baseName = baseName.substring(0, baseName.lastIndexOf("."));
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

        // filename = timeStamp + UUID + originalFilename
        String fileName = timeStamp + "_" + UUID.randomUUID() + "_" + baseName + ((extension != null) ? "." + extension : "");

        Path dirPath = Paths.get(uploadDir);
        Path uploadPath = dirPath.resolve(fileName);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("이미지를 저장할 수 없습니다 : " + fileName, ioe);
        }
        return String.format(uploadDir + "/" + fileName);
    }
}
