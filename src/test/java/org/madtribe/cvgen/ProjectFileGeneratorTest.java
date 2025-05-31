package org.madtribe.cvgen;

import org.junit.Assert;
import org.junit.Test;
import org.madtribe.cvgen.model.CVProject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ProjectFileGeneratorTest {

    private ProjectFileGenerator instance;

    @org.junit.Before
    public void setUp() throws Exception {
        instance = new ProjectFileGenerator();
    }

    @Test
    public void test_create_project_file() throws IOException {
        CVProject input = new CVProject("Mad Tribe");
        String json = instance.serializeProject(input);

        Assert.assertNotNull(json);

        ObjectMapper mapper = new ObjectMapper();

        CVProject project;
        try (var parser = mapper.createParser(json)){
              project = parser.readValueAs(CVProject.class);
        }

        Assert.assertEquals("Mad Tribe", project.fullName());

    }


}