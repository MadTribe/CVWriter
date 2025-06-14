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
    public void testFilterOnly() {
        Achievement javaAch = new Achievement("Ach Java", List.of("java"));
        Achievement pythonAch = new Achievement("Ach Python", List.of("python"));

        Project javaProject = new Project("Java Project", "desc", List.of(javaAch), List.of("java"));
        Project pythonProject = new Project("Python Project", "desc", List.of(pythonAch), List.of("python"));

        Position position = new Position("Dev",
                new Period(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)),
                "desc",
                List.of(),
                List.of(javaProject, pythonProject));

        Employer employer = new Employer("Company", "City",
                new Period(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12, 31)),
                List.of(position));

        Skill javaSkill = new Skill("Java", List.of("java"));
        Skill pythonSkill = new Skill("Python", List.of("python"));

        CVProject base = new CVProject("Tester")
                .withEmployers(List.of(employer))
                .withKeyAchievements(List.of(javaAch, pythonAch))
                .withTechnicalSkills(Map.of("Languages", List.of(javaSkill, pythonSkill)))
                .withTags(List.of("java", "python"));

        CVProject filtered = CVFilter.filter(base, List.of("java"));

        // Should filter out python-related items
        assertEquals(1, filtered.employers().size());
        Employer filteredEmployer = filtered.employers().get(0);
        assertEquals(1, filteredEmployer.positions().size());
        assertEquals(1, filteredEmployer.positions().get(0).projects().size());
        assertEquals("Java Project", filteredEmployer.positions().get(0).projects().get(0).title());
        assertEquals("Ach Java", filteredEmployer.positions().get(0).projects().get(0).achievements().get(0).name());

        assertEquals(1, filtered.keyAchievements().size());
        assertEquals("Ach Java", filtered.keyAchievements().get(0).name());

        assertEquals(1, filtered.technicalSkills().get("Languages").size());
        assertEquals("Java", filtered.technicalSkills().get("Languages").get(0).name());

        assertEquals(1, filtered.tags().size());
        assertEquals("java", filtered.tags().get(0));
    }

    @Test
    public void testSortOnly() {
        Employer older = new Employer("OldCo", "City",
                new Period(LocalDate.of(2019, 1, 1), LocalDate.of(2020, 12, 31)),
                List.of());

        Employer newer = new Employer("NewCo", "City",
                new Period(LocalDate.of(2021, 1, 1), LocalDate.of(2023, 12, 31)),
                List.of());

        Position oldPosition = new Position("Old Dev",
                new Period(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 31)),
                "desc", List.of(), List.of());

        Position newPosition = new Position("New Dev",
                new Period(LocalDate.of(2023, 1, 1), LocalDate.of(2024, 12, 31)),
                "desc", List.of(), List.of());

        older = older.withPositions(List.of(oldPosition));
        newer = newer.withPositions(List.of(newPosition));

        CVProject unsorted = new CVProject("Tester")
                .withEmployers(List.of(older, newer));

        CVProject sorted = CVFilter.sortByDateDescending(unsorted);

        // Check employers are sorted
        assertEquals("NewCo", sorted.employers().get(0).name());
        assertEquals("OldCo", sorted.employers().get(1).name());

        // Check positions are sorted
        List<Position> sortedPositions = sorted.employers().get(0).positions();
        assertEquals("New Dev", sortedPositions.get(0).title());
    }

    @Test
    public void testFilterAndSortTogether() {
        // Combine both behaviors in one pipeline
        Employer emp1 = new Employer("A", "Loc", new Period(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 12, 31)),
                List.of(new Position("P1",
                        new Period(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 12, 31)),
                        "desc", List.of(),
                        List.of(new Project("Project A", "desc",
                                List.of(new Achievement("Ach A", List.of("java"))), List.of("java")))))
        );

        Employer emp2 = new Employer("B", "Loc", new Period(LocalDate.of(2023, 1, 1), LocalDate.of(2024, 12, 31)),
                List.of(new Position("P2",
                        new Period(LocalDate.of(2023, 1, 1), LocalDate.of(2024, 12, 31)),
                        "desc", List.of(),
                        List.of(new Project("Project B", "desc",
                                List.of(new Achievement("Ach B", List.of("python"))), List.of("python")))))
        );

        CVProject base = new CVProject("Tester")
                .withEmployers(List.of(emp1, emp2));

        CVProject filteredThenSorted = CVFilter.sortByDateDescending(CVFilter.filter(base, List.of("java")));

        // Only emp1 should remain
        assertEquals(1, filteredThenSorted.employers().size());
        assertEquals("A", filteredThenSorted.employers().get(0).name());
    }
}
