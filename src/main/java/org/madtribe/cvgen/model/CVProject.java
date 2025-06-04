
package org.madtribe.cvgen.model;

import lombok.With;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@With
public record CVProject(
        String fullName,
        Contact contact,
        List<Employer> employers,
        List<Education> educations,
        String professionalSummary,
        List<Achievement> keyAchievements,
        Map<String, List<Skill>> technicalSkills, // Categorized skills
        List<String> spokenLanguages,
        List<String>  tags
) {
    public CVProject(String fullName){
        this(
                fullName,
                new Contact("", "", ""),
                Collections.emptyList(),
                Collections.emptyList(),
                "",
                Collections.emptyList(),
                Map.of(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }


    @With
    public record Contact(String phoneNumber, String email, String linkedIn) {}

    @With
    public record Period(LocalDate from, LocalDate to) {}

    @With
    public record Employer(
            String name,
            String location,
            Period period,
            List<Position> positions
    ) {}

    @With
    public record Position(
            String title,
            Period period,
            String description,
            List<String> responsibilities,
            List<Project> projects
    ) {}

    @With
    public record Project(
            String title,
            String description,
            List<Achievement> achievements,
            List<String> tags
    ) {}

    @With
    public record Education(
            String title,
            String institution,
            Period period
    ) {}

    @With
    public record Achievement(String name,
                              List<String> tags) {

    }

    @With
    public record Skill(String name,
                        List<String> tags) {

    }

}