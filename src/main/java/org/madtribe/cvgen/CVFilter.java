package org.madtribe.cvgen;


import org.madtribe.cvgen.model.CVProject;
import org.madtribe.cvgen.model.CVProject.Achievement;
import org.madtribe.cvgen.model.CVProject.Education;
import org.madtribe.cvgen.model.CVProject.Employer;
import org.madtribe.cvgen.model.CVProject.Position;
import org.madtribe.cvgen.model.CVProject.Project;
import org.madtribe.cvgen.model.CVProject.Skill;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CVFilter {

    public static CVProject filter(CVProject cv, List<String> tags) {
        Set<String> tagSet = new HashSet<>(tags);

        List<CVProject.Employer> filteredEmployers = cv.employers().stream()
                .map(emp -> emp.withPositions(
                        emp.positions().stream()
                                .map(pos -> pos.withProjects(
                                        pos.projects().stream()
                                                .filter(proj -> keepItem(proj.tags(), tagSet))
                                                .map(proj -> proj.withAchievements(
                                                        proj.achievements().stream()
                                                                .filter(ach -> keepItem(ach.tags(), tagSet))
                                                                .collect(Collectors.toList())
                                                ))
                                                .collect(Collectors.toList())
                                ))
                                .filter(pos -> !pos.projects().isEmpty()) // remove positions with no projects
                                .collect(Collectors.toList())
                ))
                .filter(emp -> !emp.positions().isEmpty()) // remove employers with no positions
                .collect(Collectors.toList());

        List<CVProject.Achievement> filteredAchievements = cv.keyAchievements().stream()
                .filter(ach -> keepItem(ach.tags(), tagSet))
                .collect(Collectors.toList());

        Map<String, List<CVProject.Skill>> filteredSkills = cv.technicalSkills().entrySet().stream()
                .map(entry -> Map.entry(
                        entry.getKey(),
                        entry.getValue().stream()
                                .filter(skill -> keepItem(skill.tags(), tagSet))
                                .collect(Collectors.toList())
                ))
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> filteredTags = cv.tags().stream()
                .filter(tagSet::contains)
                .collect(Collectors.toList());

        return cv.withEmployers(filteredEmployers)
                .withKeyAchievements(filteredAchievements)
                .withTechnicalSkills(filteredSkills)
                .withTags(filteredTags);
    }

    public static CVProject sortByDateDescending(CVProject cv) {
        List<CVProject.Employer> sortedEmployers = cv.employers().stream()
                .sorted((e1, e2) -> comparePeriodDescending(e1.period(), e2.period()))
                .map(emp -> emp.withPositions(
                        emp.positions().stream()
                                .sorted((p1, p2) -> comparePeriodDescending(p1.period(), p2.period()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return cv.withEmployers(sortedEmployers);
    }

    private static boolean keepItem(List<String> itemTags, Set<String> filterTags) {
        return itemTags == null || itemTags.isEmpty() || itemTags.stream().anyMatch(filterTags::contains);
    }

    private static int comparePeriodDescending(CVProject.Period p1, CVProject.Period p2) {
        if (p1 == null || p1.to() == null) return 1;
        if (p2 == null || p2.to() == null) return -1;
        return p2.to().compareTo(p1.to()); // newest first
    }
}
