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

    /**
     * Returns a new CVProject filtered according to the provided tags.
     * Any item that has no tags will be included by default.
     * Date‐ranged items (employers, positions, educations) are sorted by most‐recent end date descending.
     *
     * @param original the original CVProject
     * @param filterTags the list of tags to filter by (if empty or null, no tag‐based filtering is applied)
     * @return a new CVProject containing only the items whose tag sets intersect filterTags (or have no tags)
     */
    public static CVProject filter(CVProject original, List<String> filterTags) {
        // If filterTags is null, treat it as empty ⇒ include everything.
        Set<String> tagSet = (filterTags == null)
                ? Collections.emptySet()
                : new HashSet<>(filterTags);

        // 1. Filter & sort Employers (and their nested Positions/Projects/Achievements)
        List<Employer> filteredEmployers = original.employers().stream()
                // For each employer, we keep it, but we filter its positions/projects down the tree
                .map(emp -> {
                    // Sort & filter Positions within this Employer
                    List<Position> filteredPositions = emp.positions().stream()
                            .map(pos -> {
                                // 1a. Filter Projects inside each Position
                                List<Project> filteredProjects = pos.projects().stream()
                                        .filter(proj -> {
                                            List<String> projTags = proj.tags();
                                            // include if no tags, or if any tag is in filterTags
                                            return projTags == null
                                                    || projTags.isEmpty()
                                                    || projTags.stream().anyMatch(tagSet::contains);
                                        })
                                        .map(proj -> {
                                            // 1b. Also filter Achievements inside each Project
                                            List<Achievement> projAchievements = proj.achievements().stream()
                                                    .filter(ach -> {
                                                        List<String> achTags = ach.tags();
                                                        return achTags == null
                                                                || achTags.isEmpty()
                                                                || achTags.stream().anyMatch(tagSet::contains);
                                                    })
                                                    .collect(Collectors.toList());
                                            return proj.withAchievements(projAchievements);
                                        })
                                        .collect(Collectors.toList());

                                // Rebuild Position with filtered projects, but preserve other fields
                                Position newPos = pos.withProjects(filteredProjects);

                                return newPos;
                            })
                            // 1c. Sort Positions by descending end date
                            .sorted(Comparator.comparing((Position p) -> {
                                LocalDate end = p.period().to();
                                return (end != null ? end : LocalDate.MIN);
                            }).reversed())
                            .collect(Collectors.toList());

                    // 1d. Rebuild Employer with the filtered/sorted positions
                    Employer newEmp = emp.withPositions(filteredPositions);

                    return newEmp;
                })
                // 1e. Sort Employers by descending end date
                .sorted(Comparator.comparing((Employer e) -> {
                    LocalDate end = e.period().to();
                    return (end != null ? end : LocalDate.MIN);
                }).reversed())
                .collect(Collectors.toList());

        // 2. Filter & sort Educations
        List<Education> filteredEducations = original.educations().stream()
                // No tag‐based filtering for Education (no tags in the record), so just sort
                .sorted(Comparator.comparing((Education ed) -> {
                    LocalDate end = ed.period().to();
                    return (end != null ? end : LocalDate.MIN);
                }).reversed())
                .collect(Collectors.toList());

        // 3. Filter top‐level Achievements (keyAchievements)
        List<Achievement> filteredAchievements = original.keyAchievements().stream()
                .filter(ach -> {
                    List<String> achTags = ach.tags();
                    return achTags == null
                            || achTags.isEmpty()
                            || achTags.stream().anyMatch(tagSet::contains);
                })
                .collect(Collectors.toList());

        // 4. Filter technicalSkills (Map<String, List<Skill>>)
        Map<String, List<Skill>> filteredSkills = original.technicalSkills().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(skill -> {
                                    List<String> skTags = skill.tags();
                                    return skTags == null
                                            || skTags.isEmpty()
                                            || skTags.stream().anyMatch(tagSet::contains);
                                })
                                .collect(Collectors.toList())
                ));

        // 5. Filter spokenLanguages? (no tags ⇒ include all by default)
        List<String> filteredLanguages = List.copyOf(original.spokenLanguages());

        // 6. Top‐level tags on CVProject: preserve only those that intersect filterTags (or untagged entries)
        List<String> filteredCvTags = original.tags().stream()
                .filter(t -> tagSet.isEmpty() || tagSet.contains(t))
                .collect(Collectors.toList());

        
        // 7. Reconstruct and return a new CVProject with all filtered/sorted bits
        return original
                .withEmployers(filteredEmployers)
                .withEducations(filteredEducations)
                .withKeyAchievements(filteredAchievements)
                .withTechnicalSkills(filteredSkills)
                .withSpokenLanguages(filteredLanguages)
                .withTags(filteredCvTags);
    }
}
