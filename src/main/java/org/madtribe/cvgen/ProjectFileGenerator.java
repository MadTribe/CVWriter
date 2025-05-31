package org.madtribe.cvgen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.madtribe.cvgen.model.CVProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectFileGenerator {

    private final ObjectMapper objectMapper;

    public ProjectFileGenerator() {
        objectMapper = new ObjectMapper();
    }

    public boolean init(String fullName, String file) throws IOException {
        CVProject project = new CVProject(fullName);
        String json = serializeProject(project);
        return writeUsingFiles( file, json);
    }

    public String serializeProject(CVProject input) throws JsonProcessingException {
        return objectMapper.writeValueAsString(input);
    }

    private static boolean writeUsingFiles(String path, String data) throws IOException {
        Path filePath = Paths.get(path);

        if (filePath.toFile().exists()){
            System.err.println("File Already Exists");
            return false;
        }

        Files.write(filePath, data.getBytes());
        return true;
    }
}
