package org.madtribe.cvgen;

import org.madtribe.cvgen.model.CVProject;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExampleDataGenerator {
    public static CVProject generateSampleCV(String fullName) {
        if (fullName == null) {
            fullName = "TBD";
        }

        return new CVProject(
                fullName,
                new CVProject.Contact("TBD", "TBD", "TBD"),
                generateEmployers(),
                generateEducations(),
                "TBD",
                List.of(
                ),
                Map.of(
                        "Programming Languages",
                        List.of()
                ),
                List.of(),
                List.of()
        );
    }

    private static List<CVProject.Employer> generateEmployers() {
        return List.of();
    }

    private static List<CVProject.Education> generateEducations() {
        return List.of(
        );
    }
}

