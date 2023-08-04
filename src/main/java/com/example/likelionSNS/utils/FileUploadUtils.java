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

@Slf4j
public class FileUploadUtils {

    // 이미지 저장
    public static String saveFile(String uploadDir, String originalFileName, MultipartFile multipartFile) throws IOException {
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        String fileName = timeStamp + ((extension != null) ? "." + extension : "");

        Path uploadPath = Paths.get(uploadDir, fileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("이미지를 저장할 수 없습니다 : " + fileName, ioe);
        }
        return String.format("/" + uploadDir + "/" + fileName);
    }
}
