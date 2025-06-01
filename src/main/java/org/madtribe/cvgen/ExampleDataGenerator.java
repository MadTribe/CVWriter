package org.madtribe.cvgen;

import org.madtribe.cvgen.model.CVProject;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExampleDataGenerator {
    public static CVProject generateSampleCV(String fullName) {
        if (fullName == null){
            fullName = "Emma Johnson";
        }
        return new CVProject(
                fullName,
                new CVProject.Contact(
                        "+44 1234 567890",
                        "john.smith@gmail.com",
                        "https://www.linkedin.com/in/johnsmith"
                ),
                List.of(
                        // VML/GTB
                        new CVProject.Employer(
                                "VML/GTB – Global (London, Shanghai)",
                                "London/Shanghai",
                                new CVProject.Period(LocalDate.of(2017, 1, 1), LocalDate.now()),
                                List.of(
                                        new CVProject.Position(
                                                "Group Technology Director",
                                                new CVProject.Period(LocalDate.of(2023, 1, 1), LocalDate.now()),
                                                "Set global technology direction for Ford/Lincoln accounts",
                                                List.of(
                                                        "Architected and led cloud migrations to GCP",
                                                        "Scaled and managed global teams (>50 engineers)",
                                                        "Oversaw BAU, platform upgrades and app development"
                                                ),
                                                List.of(
                                                        new CVProject.Project(
                                                                "Ford/Lincoln Cloud Migration",
                                                                "Global migration of platforms to GCP",
                                                                List.of(
                                                                        "Achieved 20% cost savings and 30% faster deployment",
                                                                        "Implemented Infrastructure as Code & automated security"
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        // Other employers...
                ),
                List.of(
                        new CVProject.Education(
                                "BSc, Physics with Study in Germany",
                                "University of Bristol",
                                new CVProject.Period(LocalDate.of(1995, 9, 1), LocalDate.of(1999, 7, 1))
                        )
                ),
                "Strategic and hands-on Software Director with 25+ years leading global teams...",
                List.of(
                        "Led global migration of Ford/Lincoln platforms to GCP: 20% cost savings, 30% faster deployment",
                        "Established FordPass China team and FordLabs Shanghai",
                        "Drove $1.6M annual revenue by scaling regional delivery teams"
                ),
                List.of(
                        "Global team leadership (up to 50 engineers)",
                        "Technology strategy & digital transformation",
                        "Client advisory for Fortune 500s"
                ),
                Map.of(
                        "Marketing Technologies", List.of("Adobe Marketing Cloud", "Adobe Analytics", "Google Analytics"),
                        "Languages", List.of("Java (8–13)", "TypeScript", "JavaScript", "Python3"),
                        "Cloud & DevOps", List.of("GCP", "AWS", "Docker", "Terraform", "Jenkins")
                ),
                List.of(
                        "English (Native)",
                        "Mandarin (Professional)"
                )
        );
    }
}
