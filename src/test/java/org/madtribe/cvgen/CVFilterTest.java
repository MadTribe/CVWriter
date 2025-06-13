package org.madtribe.cvgen;

import org.junit.Test;
import org.madtribe.cvgen.CVFilter;
import org.madtribe.cvgen.model.CVProject;
import org.madtribe.cvgen.model.CVProject.Achievement;
import org.madtribe.cvgen.model.CVProject.Employer;
import org.madtribe.cvgen.model.CVProject.Period;
import org.madtribe.cvgen.model.CVProject.Position;
import org.madtribe.cvgen.model.CVProject.Project;
import org.madtribe.cvgen.model.CVProject.Skill;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CVFilterTest {

    @Test
    public void testFilterByTagsAndSorting() {
        // --- Build sample Achievements with tags ---
        Achievement achJava = new Achievement("Achievement Java", List.of("java"));
        Achievement achPython = new Achievement("Achievement Python", List.of("python"));

        // --- Build sample Projects with tags and nested achievements ---
        Project projectJava = new Project(
                "Project Java",
                "Java project description",
                List.of(achJava),
                List.of("java")
        );
        Project projectPython = new Project(
                "Project Python",
                "Python project description",
                List.of(achPython),
                List.of("python")
        );

        // --- Build a Position containing both projects; period ends 2023-12-31 ---
        Position positionDev = new Position(
                "Developer",
                new Period(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)),
                "Developed software",
                List.of("Coding", "Unit tests"),
                List.of(projectJava, projectPython)
        );

        // --- Employer A: period ends 2023-12-31, has one position ---
        Employer employerA = new Employer(
                "CompanyA",
                "CityA",
                new Period(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12, 31)),
                List.of(positionDev)
        );

        // --- Employer B: period ends 2022-12-31, has no positions (for testing sorting only) ---
        Employer employerB = new Employer(
                "CompanyB",
                "CityB",
                new Period(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 12, 31)),
                List.of()
        );

        // --- Top‐level Achievements ---
        List<Achievement> topAchievements = List.of(achJava, achPython);

        // --- Technical Skills: one category “Languages” with two skills ---
        Skill skillJava = new Skill("Java", List.of("java"));
        Skill skillPython = new Skill("Python", List.of("python"));
        Map<String, List<Skill>> technicalSkills = Map.of(
                "Languages",
                List.of(skillJava, skillPython)
        );

        // --- Top‐level CV tags ---
        List<String> cvTags = List.of("java", "management");

        // --- Construct the base CVProject with employers in reversed order (to test sorting) ---
        CVProject baseCv = new CVProject("John Doe")
                .withEmployers(List.of(employerB, employerA))
                .withKeyAchievements(topAchievements)
                .withTechnicalSkills(technicalSkills)
                .withTags(cvTags);

        // --- Apply filter: only include items tagged “java” ---
        CVProject filteredCv = CVFilter.filter(baseCv, List.of("java"));

        // --- 1. Verify employers are sorted by most‐recent end date (employerA first) ---
        assertEquals("Should still include both employers (with filtered positions).", 2, filteredCv.employers().size());
        assertEquals(
                "Employer A should come before Employer B because its end date is later (2023-12-31 vs 2022-12-31).",
                "CompanyA",
                filteredCv.employers().get(0).name()
        );
        assertEquals("CompanyB", filteredCv.employers().get(1).name());

        // --- 2. Verify positions:
        //     - Employer A has one position; Employer B has none.
        //     - Within Employer A’s position, only the Java project remains ---
        Employer filteredA = filteredCv.employers().get(0);
        assertEquals("Employer A should still have one position.", 1, filteredA.positions().size());

        Position filteredPosition = filteredA.positions().get(0);
        assertEquals("Developer", filteredPosition.title());
        // Two projects originally; Python‐tagged project should be filtered out
        assertEquals("Only one project (the one tagged “java”) should remain.", 1, filteredPosition.projects().size());
        assertEquals("Project Java", filteredPosition.projects().get(0).title());

        // Employer B had no positions to begin with, so its list stays empty
        Employer filteredB = filteredCv.employers().get(1);
        assertTrue("Employer B should have no positions.", filteredB.positions().isEmpty());

        // --- 3. Verify nested achievements: only the Java achievement remains within the kept Java project ---
        Project keptProject = filteredPosition.projects().get(0);
        assertEquals(
                "The Java project’s achievements should be filtered to only those tagged “java”.",
                1,
                keptProject.achievements().size()
        );
        assertEquals("Achievement Java", keptProject.achievements().get(0).name());

        // --- 4. Verify top‐level achievements were filtered by tag “java” ---
        assertEquals(
                "Top‐level achievements should be filtered so only the one tagged “java” remains.",
                1,
                filteredCv.keyAchievements().size()
        );
        assertEquals("Achievement Java", filteredCv.keyAchievements().get(0).name());

        // --- 5. Verify technicalSkills: “Python” skill is removed, “Java” remains ---
        assertTrue("Category “Languages” should still exist.", filteredCv.technicalSkills().containsKey("Languages"));
        List<Skill> filteredLangSkills = filteredCv.technicalSkills().get("Languages");
        assertEquals("Only the Java skill should remain under “Languages”.", 1, filteredLangSkills.size());
        assertEquals("Java", filteredLangSkills.get(0).name());

        // --- 6. Verify top‐level CV tags: only “java” remains, “management” is dropped ---
        assertEquals("Only “java” should remain in the top‐level CV tags.", 1, filteredCv.tags().size());
        assertEquals("java", filteredCv.tags().get(0));
    }
}
