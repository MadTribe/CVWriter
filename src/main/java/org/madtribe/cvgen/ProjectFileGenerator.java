package org.madtribe.cvgen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.madtribe.cvgen.model.CVProject;

public class ProjectFileGenerator {

    private final ObjectMapper objectMapper;

    public ProjectFileGenerator() {
        objectMapper = new ObjectMapper();
    }

    public String serializeProject(CVProject input) throws JsonProcessingException {
        return objectMapper.writeValueAsString(input);
    }
}
