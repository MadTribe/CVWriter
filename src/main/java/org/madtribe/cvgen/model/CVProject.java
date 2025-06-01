package org.madtribe.cvgen.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public record CVProject(
        String fullName,
        Contact contact,
        List<Employer> employers,
        List<Education> educations,
        String professionalSummary,
        List<String> keyAchievements,
        Map<String, List<String>> technicalSkills, // Categorized skills
        List<String> spokenLanguages
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
                Collections.emptyList()
        );
    }


    public record Contact(String phoneNumber, String email, String linkedIn) {}

    public record Period(LocalDate from, LocalDate to) {}

    public record Employer(
            String name,
            String location,
            Period period,
            List<Position> positions
    ) {}

    public record Position(
            String title,
            Period period,
            String description,
            List<String> responsibilities,
            List<Project> projects
    ) {}

    public record Project(
            String title,
            String description,
            List<String> achievements
    ) {}

    public record Education(
            String title,
            String institution,
            Period period
    ) {}
}