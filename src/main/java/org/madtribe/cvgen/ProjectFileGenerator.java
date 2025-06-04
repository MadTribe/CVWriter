package org.madtribe.cvgen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.madtribe.cvgen.model.CVProject;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class ProjectFileGenerator {

    private final ObjectMapper objectMapper;

    public ProjectFileGenerator() {
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    }

    public CVProject init(String fullName, String file) throws IOException {
        var contact = new CVProject.Contact("","","");
        var period = new CVProject.Period(LocalDate.of(2024,12,04)
                ,LocalDate.of(2024,12, 04));

        CVProject project = ExampleDataGenerator.generateSampleCV(fullName);
        String json = serializeProject(project);
         writeUsingFiles( file, false, json);
        return project;
    }

    public String serializeProject(CVProject input) throws JsonProcessingException {
        return objectMapper.writeValueAsString(input);
    }

    public static void writeUsingFiles(String path, boolean overwrite, String data) throws IOException {
        Path filePath = Paths.get(path);

        if (filePath.toFile().exists() && !overwrite) {
            System.err.println("File Already Exists");
            throw new FileAlreadyExistsException(path);
        }

        Files.write(filePath, data.getBytes());
    }
}
