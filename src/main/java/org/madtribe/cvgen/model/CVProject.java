package org.madtribe.cvgen.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record CVProject(String fullName,
                        Contact contact,
                        List<Employer> employers,
                        List<Skill> skills,
                        List<Education> educations
                        ) {
    public CVProject(String fullName){
        this(
                fullName,
                new Contact("", "", ""),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    public record Contact( String phoneNumber, String email, String linkedIn) {
    }

    public record Period(LocalDate from, LocalDate to){
    }

    public record Employer(String name,String location ,Period period, List<Position> position, List<Project> projects)  {
    }

    public record Position(String title, String description, Period period) {
    }

    public record Project(String title, String description, Period period, List<Skill> skills) {
    }

    public record Skill(String title) {
    }
    public record Education(String title, String institution, Period period) {
    }

    public record Qualification(String title, String institution) {
    }
}
