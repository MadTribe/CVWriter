package org.madtribe.cvgen;

import org.madtribe.cvgen.model.CVProject;

import java.time.LocalDate;
import java.util.List;

public class ExampleDataGenerator {
    public static CVProject generateSampleCV(String fullName) {
        if (fullName == null){
            fullName = "Emma Johnson";
        }
        return new CVProject(
                fullName,
                new CVProject.Contact("+1 (555) 123-4567", "emma.johnson@career.com", "linkedin.com/in/emma-johnson-dev"),
                generateEmployers(),
                generateSkills(),
                generateEducations()
        );
    }

    private static List<CVProject.Skill> generateSkills() {
        return List.of(
                new CVProject.Skill("Java"),
                new CVProject.Skill("Spring Boot"),
                new CVProject.Skill("Cloud Architecture"),
                new CVProject.Skill("Project Leadership"),
                new CVProject.Skill("Kubernetes"),
                new CVProject.Skill("SQL"),
                new CVProject.Skill("React")
        );
    }

    private static List<CVProject.Education> generateEducations() {
        return List.of(
                new CVProject.Education(
                        "MSc Computer Science",
                        "Tech University",
                        new CVProject.Period(LocalDate.of(2016, 9, 1), LocalDate.of(2018, 6, 15))
                ),
                new CVProject.Education(
                        "BSc Software Engineering",
                        "State College",
                        new CVProject.Period(LocalDate.of(2012, 9, 10), LocalDate.of(2016, 6, 1))
                )
        );
    }

    private static List<CVProject.Employer> generateEmployers() {
        return List.of(
                new CVProject.Employer(
                        "InnovateTech Solutions",
                        "San Francisco, CA",
                        new CVProject.Period(LocalDate.of(2020, 3, 1), LocalDate.of(2025, 5, 31)),
                        List.of(
                                new CVProject.Position(
                                        "Senior Solutions Architect",
                                        "Lead cloud migration initiatives for enterprise clients",
                                        new CVProject.Period(LocalDate.of(2022, 6, 1), LocalDate.of(2025, 5, 31))
                                ),
                                new CVProject.Position(
                                        "Software Engineer",
                                        "Developed microservices architecture for SaaS platform",
                                        new CVProject.Period(LocalDate.of(2020, 3, 1), LocalDate.of(2022, 5, 31))
                                )
                        ),
                        List.of(
                                new CVProject.Project(
                                        "Cloud Transformation",
                                        "Migrated legacy monolith to AWS microservices serving 500k users",
                                        new CVProject.Period(LocalDate.of(2021, 1, 15), LocalDate.of(2022, 9, 30)),
                                        List.of(
                                                new CVProject.Skill("Cloud Architecture"),
                                                new CVProject.Skill("AWS"),
                                                new CVProject.Skill("Kubernetes")
                                        )
                                )
                        )
                ),

                new CVProject.Employer(
                        "DataDynamics Inc.",
                        "Boston, MA",
                        new CVProject.Period(LocalDate.of(2018, 7, 10), LocalDate.of(2020, 2, 28)),
                        List.of(
                                new CVProject.Position(
                                        "Full Stack Developer",
                                        "Implemented data visualization dashboard with React frontend",
                                        new CVProject.Period(LocalDate.of(2018, 7, 10), LocalDate.of(2020, 2, 28))
                                )
                        ),
                        List.of(
                                new CVProject.Project(
                                        "Analytics Platform v2",
                                        "Redesigned data processing pipeline improving performance by 40%",
                                        new CVProject.Period(LocalDate.of(2019, 3, 1), LocalDate.of(2019, 11, 15)),
                                        List.of(
                                                new CVProject.Skill("Java"),
                                                new CVProject.Skill("React"),
                                                new CVProject.Skill("SQL")
                                        )
                                )
                        )
                )
        );
    }
}
