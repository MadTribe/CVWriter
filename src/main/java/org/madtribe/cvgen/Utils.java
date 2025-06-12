package org.madtribe.cvgen;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String addTimestampToFileName(String filePath) {
        Path originalPath = Paths.get(filePath);
        Path parent = originalPath.getParent();
        String fileName = originalPath.getFileName().toString();

        int dotIndex = fileName.lastIndexOf('.');
        String baseName = fileName;
        String extension = "";
        if (dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex + 1);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm_ss");
        String timestamp = LocalDateTime.now().format(formatter);

        String newFileName;
        if (!extension.isEmpty()) {
            newFileName = baseName + "_" + timestamp + "." + extension;
        } else {
            newFileName = baseName + "_" + timestamp;
        }

        if (parent != null) {
            return parent.resolve(newFileName).toString();
        } else {
            return newFileName;
        }
    }
}
