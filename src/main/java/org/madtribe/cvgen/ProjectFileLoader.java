package org.madtribe.cvgen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.madtribe.cvgen.model.CVProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ProjectFileLoader {

    private final ObjectMapper objectMapper;

    public ProjectFileLoader() {
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    }

    public CVProject load(String file) throws IOException {
        CVProject project;

        String json = Files.readString(new File(file).toPath());

        try (var parser = objectMapper.createParser(json)){
            project = parser.readValueAs(CVProject.class);
        }

        return project;
    }

}
