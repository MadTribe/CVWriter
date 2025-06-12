package org.madtribe.cvgen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.madtribe.cvgen.model.CVProject;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class CVProjectExporterImporterTest {

    @TempDir
    Path tempDir;

    // Helper method to create a sample CVProject
    private CVProject createSampleCV() {
        // Contact information
        CVProject.Contact contact = new CVProject.Contact(
                "123-456-7890",
                "john.doe@example.com",
                "linkedin.com/in/johndoe"
        );

        // Education
        CVProject.Education education = new CVProject.Education(
                "Bachelor of Science in Computer Science",
                "University of Example",
                new CVProject.Period(LocalDate.of(2015, 9, 1), LocalDate.of(2019, 6, 15))
        );

        // Skills
        CVProject.Skill javaSkill = new CVProject.Skill("Java", List.of("backend", "oop"));
        CVProject.Skill springSkill = new CVProject.Skill("Spring Boot", List.of("framework", "backend"));
        Map<String, List<CVProject.Skill>> technicalSkills = Map.of(
                "Programming Languages", List.of(javaSkill),
                "Frameworks", List.of(springSkill)
        );

        // Achievements
        CVProject.Achievement achievement1 = new CVProject.Achievement(
                "Reduced server costs by 30%",
                List.of("cost-saving", "optimization")
        );
        CVProject.Achievement achievement2 = new CVProject.Achievement(
                "Implemented CI/CD pipeline",
                List.of("devops", "automation")
        );

        // Project
        CVProject.Project project = new CVProject.Project(
                "E-commerce Platform",
                "Developed a full-featured online shopping platform",
                List.of(
                        new CVProject.Achievement("Implemented payment gateway", List.of("integration")),
                        new CVProject.Achievement("Optimized database queries", List.of("performance"))
                ),
                List.of("java", "spring", "react")
        );

        // Position
        CVProject.Position position = new CVProject.Position(
                "Senior Software Engineer",
                new CVProject.Period(LocalDate.of(2020, 3, 1), LocalDate.of(2023, 5, 31)),
                "Led backend development team",
                List.of("Code reviews", "Technical design", "Mentoring"),
                List.of(project)
        );

        // Employer
        CVProject.Employer employer = new CVProject.Employer(
                "Tech Innovations Inc.",
                "San Francisco, CA",
                new CVProject.Period(LocalDate.of(2020, 3, 1), LocalDate.of(2023, 5, 31)),
                List.of(position)
        );

        return new CVProject(
                "John Doe",
                contact,
                List.of(employer),
                List.of(education),
                "Experienced software engineer with 5+ years in backend development",
                Collections.emptyList(), // TODO
                List.of(achievement1, achievement2),
                technicalSkills,
                List.of("English", "Spanish"),
                List.of("backend", "java", "spring")
        );
    }

    @Test
    void testExportImportRoundTrip() throws Exception {
        // Create sample CV
        CVProject original = createSampleCV();

        // Export to temp directory
        CVProjectExporterImporter.export(original, tempDir);

        // Import back from directory
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        // Verify the round trip
        Assertions.assertEquals(original, imported, "Original and imported CV should be equal");
    }

    @Test
    void testEmptyCVExportImport() throws Exception {
        // Create minimal CV with just a name
        CVProject minimalCV = new CVProject("Jane Smith");

        // Export to temp directory
        CVProjectExporterImporter.export(minimalCV, tempDir);

        // Import back from directory
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        // Verify basic properties
        assertEquals(minimalCV.fullName(), imported.fullName());
        assertTrue(imported.employers().isEmpty());
        assertTrue(imported.educations().isEmpty());
        assertTrue(imported.keyAchievements().isEmpty());
        assertTrue(imported.technicalSkills().isEmpty());
    }

    @Test
    void testContactInformation() throws Exception {
        CVProject.Contact contact = new CVProject.Contact("555-1234", "test@example.com", "linkedin.com/test");
        CVProject cv = new CVProject("Contact Test")
                .withContact(contact);

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        assertEquals(contact, imported.contact());
    }

    @Test
    void testMultipleEmployers() throws Exception {
        CVProject.Employer employer1 = new CVProject.Employer(
                "Company A",
                "Location A",
                new CVProject.Period(LocalDate.of(2018, 1, 1), LocalDate.of(2020, 1, 1)),
                List.of()
        );

        CVProject.Employer employer2 = new CVProject.Employer(
                "Company B",
                "Location B",
                new CVProject.Period(LocalDate.of(2020, 2, 1), LocalDate.of(2022, 3, 31)),
                List.of()
        );

        CVProject cv = new CVProject("Multi-Employer Test")
                .withEmployers(List.of(employer1, employer2));

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        assertEquals(2, imported.employers().size());
        assertEquals(employer1, imported.employers().get(0));
        assertEquals(employer2, imported.employers().get(1));
    }

    @Test
    void testNestedProjects() throws Exception {
        CVProject.Project project1 = new CVProject.Project(
                "Project Alpha",
                "First project description",
                List.of(),
                List.of("tag1", "tag2")
        );

        CVProject.Project project2 = new CVProject.Project(
                "Project Beta",
                "Second project description",
                List.of(
                        new CVProject.Achievement("Achievement 1", List.of("success")),
                        new CVProject.Achievement("Achievement 2", List.of("innovation"))
                ),
                List.of("tag3")
        );

        CVProject.Position position = new CVProject.Position(
                "Developer",
                new CVProject.Period(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31)),
                "Position description",
                List.of("Responsibility 1", "Responsibility 2"),
                List.of(project1, project2)
        );

        CVProject.Employer employer = new CVProject.Employer(
                "Company X",
                "Location X",
                new CVProject.Period(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31)),
                List.of(position)
        );

        CVProject cv = new CVProject("Nested Projects Test")
                .withEmployers(List.of(employer));

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        // Verify nested structure
        CVProject.Project importedProject1 = imported.employers().get(0)
                .positions().get(0)
                .projects().get(0);

        CVProject.Project importedProject2 = imported.employers().get(0)
                .positions().get(0)
                .projects().get(1);

        assertEquals(project1.title(), importedProject1.title());
        assertEquals(project2.title(), importedProject2.title());
        assertEquals(2, importedProject2.achievements().size());
    }

    @Test
    void testTechnicalSkillsCategories() throws Exception {
        CVProject.Skill skill1 = new CVProject.Skill("Python", List.of("backend"));
        CVProject.Skill skill2 = new CVProject.Skill("JavaScript", List.of("frontend"));
        CVProject.Skill skill3 = new CVProject.Skill("Docker", List.of("devops"));

        Map<String, List<CVProject.Skill>> skills = Map.of(
                "Languages", List.of(skill1, skill2),
                "Tools", List.of(skill3)
        );

        CVProject cv = new CVProject("Skills Test")
                .withTechnicalSkills(skills);

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        assertEquals(2, imported.technicalSkills().size());
        assertEquals(2, imported.technicalSkills().get("Languages").size());
        assertEquals(1, imported.technicalSkills().get("Tools").size());
    }

    @Test
    void testAchievementsWithTags() throws Exception {
        CVProject.Achievement achievement1 = new CVProject.Achievement(
                "Innovation Award 2022",
                List.of("recognition", "award")
        );

        CVProject.Achievement achievement2 = new CVProject.Achievement(
                "Open Source Contributor",
                List.of("community", "github")
        );

        CVProject cv = new CVProject("Achievements Test")
                .withKeyAchievements(List.of(achievement1, achievement2));

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        assertEquals(2, imported.keyAchievements().size());
        assertEquals(List.of("recognition", "award"), imported.keyAchievements().get(0).tags());
        assertEquals(List.of("community", "github"), imported.keyAchievements().get(1).tags());
    }

    @Test
    void testDateHandling() throws Exception {
        LocalDate start = LocalDate.of(2020, 1, 15);
        LocalDate end = LocalDate.of(2023, 8, 31);

        CVProject.Period period = new CVProject.Period(start, end);
        CVProject.Education education = new CVProject.Education(
                "Test Degree",
                "Test University",
                period
        );

        CVProject cv = new CVProject("Date Test")
                .withEducations(List.of(education));

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        CVProject.Period importedPeriod = imported.educations().get(0).period();
        assertEquals(start, importedPeriod.from());
        assertEquals(end, importedPeriod.to());
    }

    @Test
    void testEmptyCollections() throws Exception {
        CVProject cv = new CVProject("Empty Collections Test")
                .withEmployers(Collections.emptyList())
                .withEducations(Collections.emptyList())
                .withKeyAchievements(Collections.emptyList())
                .withTechnicalSkills(Map.of())
                .withSpokenLanguages(Collections.emptyList())
                .withTags(Collections.emptyList());

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        assertTrue(imported.employers().isEmpty());
        assertTrue(imported.educations().isEmpty());
        assertTrue(imported.keyAchievements().isEmpty());
        assertTrue(imported.technicalSkills().isEmpty());
        assertTrue(imported.spokenLanguages().isEmpty());
        assertTrue(imported.tags().isEmpty());
    }

    @Test
    void testSpecialCharactersInFilenames() throws Exception {
        // Category name with special characters
        Map<String, List<CVProject.Skill>> skills = Map.of(
                "Cloud & DevOps", List.of(new CVProject.Skill("AWS", List.of("cloud")))
        );

        CVProject cv = new CVProject("Special Chars Test")
                .withTechnicalSkills(skills);

        CVProjectExporterImporter.export(cv, tempDir);
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);

        assertTrue(imported.technicalSkills().containsKey("Cloud & DevOps"));
        assertEquals(1, imported.technicalSkills().get("Cloud & DevOps").size());
    }

    @Test
    void testLargeDataSet() throws Exception {
        // Create a CV with multiple employers, each with multiple positions, etc.
        List<CVProject.Employer> employers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<CVProject.Position> positions = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                List<CVProject.Project> projects = new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    projects.add(new CVProject.Project(
                            "Project " + k,
                            "Description " + k,
                            List.of(),
                            List.of()
                    ));
                }
                positions.add(new CVProject.Position(
                        "Position " + j,
                        new CVProject.Period(LocalDate.of(2015 + j, 1, 1), LocalDate.of(2016 + j, 12, 31)),
                        "Description " + j,
                        List.of(),
                        projects
                ));
            }
            employers.add(new CVProject.Employer(
                    "Company " + i,
                    "Location " + i,
                    new CVProject.Period(LocalDate.of(2010 + i, 1, 1), LocalDate.of(2020 + i, 12, 31)),
                    positions
            ));
        }

        CVProject cv = new CVProject("Large Dataset Test")
                .withEmployers(employers);

        // Test export and import
        long startTime = System.currentTimeMillis();
        CVProjectExporterImporter.export(cv, tempDir);
        long exportTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        CVProject imported = CVProjectExporterImporter.importCV(tempDir);
        long importTime = System.currentTimeMillis() - startTime;

        // Verify structure
        assertEquals(10, imported.employers().size());
        assertEquals(5, imported.employers().get(0).positions().size());
        assertEquals(3, imported.employers().get(0).positions().get(0).projects().size());

        System.out.printf("Large dataset test - Export: %dms, Import: %dms%n", exportTime, importTime);
    }
}