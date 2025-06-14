package org.madtribe.cvgen;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

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



        public static void copyFolder(Path source, Path target) throws IOException {
            // Delete existing destination first (like cp -R behavior)
            if (Files.exists(target)) {
                throw new IOException("destination already exists.");
            }

            // Recursive copy using Files.walk
            try (var stream = Files.walk(source)) {
                stream.forEach(s -> {
                    try {
                        Path d = target.resolve(source.relativize(s));
                        if (Files.isDirectory(s)) {
                            Files.createDirectories(d);  // Create directory structure
                        } else {
                            Files.copy(s, d, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to copy: " + s, e);
                    }
                });
            }
        }

        private static void deleteRecursive(Path path) throws IOException {
            try (var stream = Files.walk(path)) {
                stream.sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try { Files.delete(p); }
                            catch (IOException e) { /* Ignore deletion errors */ }
                        });
            }
        }
    }
