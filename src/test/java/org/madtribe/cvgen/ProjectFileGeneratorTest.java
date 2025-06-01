package org.madtribe.cvgen;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Test;
import org.madtribe.cvgen.model.CVProject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
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

        ObjectMapper mapper =  JsonMapper.builder().addModule(new JavaTimeModule()).build();

        CVProject project;
        try (var parser = mapper.createParser(json)){
              project = parser.readValueAs(CVProject.class);
        }

        Assert.assertEquals("Mad Tribe", project.fullName());

    }


    @Test
    public void test_init_project() throws IOException {
        var tempFile = File.createTempFile("test","json");
        var path = tempFile.getPath() + "1";
        var outputFile = new File(path);

        Assert.assertFalse(outputFile.exists());

        CVProject project = instance.init("m t", path);

        Assert.assertTrue(outputFile.exists());
        Assert.assertNotNull(project);

        try {
            instance.init("m t", path);
            Assert.fail("Exception not thrown");
        } catch (IOException e) {
            // intentinoally empty
        }
    }
}