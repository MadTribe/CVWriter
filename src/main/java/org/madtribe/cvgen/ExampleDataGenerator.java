package org.madtribe.cvgen;

import org.madtribe.cvgen.model.CVProject;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExampleDataGenerator {
    public static CVProject generateSampleCV(String fullName) {
        if (fullName == null) {
            fullName = "Emma Johnson";
        }

        return new CVProject(
                fullName,
                new CVProject.Contact("+44 7911 123456", "alex.morgan@dev.com", "linkedin.com/in/alex-morgan-dev"),
                generateEmployers(),
                generateEducations(),
                "Senior Software Engineer with 8+ years of experience in full-stack development and cloud architecture. Proven track record of leading high-impact projects.",
                List.of(
                        "Reduced API latency by 70% through architectural optimizations",
                        "Implemented CI/CD pipeline reducing deployment time by 80%",
                        "Mentored 15+ junior developers across 3 engineering teams"
                ),
                Map.of(
                        "Programming Languages",
                        List.of(
                                new CVProject.Skill("Java", List.of("Backend")),
                                new CVProject.Skill("TypeScript", List.of("Frontend"))
                        ),
                        "Cloud Technologies",
                        List.of(
                                new CVProject.Skill("AWS", List.of("EC2", "S3")),
                                new CVProject.Skill("Azure", List.of("Functions", "CosmosDB"))
                        ),
                        "Frameworks",
                        List.of(
                                new CVProject.Skill("Spring Boot", List.of("Microservices")),
                                new CVProject.Skill("React", List.of("Hooks", "Context API"))
                        )
                ),
                List.of("English", "Spanish", "French"),
                List.of("Microservices", "Frontend", "Backend")
        );
    }

    private static List<CVProject.Employer> generateEmployers() {
        return List.of(
                new CVProject.Employer(
                        "TechInnovate Ltd",
                        "London, UK",
                        new CVProject.Period(LocalDate.of(2019, 6, 1), LocalDate.of(2025, 5, 31)),
                        List.of(
                                new CVProject.Position(
                                        "Lead Software Engineer",
                                        new CVProject.Period(LocalDate.of(2022, 3, 1), LocalDate.of(2025, 5, 31)),
                                        "Technical lead for payments platform",
                                        List.of(
                                                "Architected microservices ecosystem",
                                                "Managed cross-functional engineering team",
                                                "Defined technical roadmap"
                                        ),
                                        List.of(
                                                new CVProject.Project(
                                                        "Payment Gateway Modernization",
                                                        "Replaced legacy payment processor with scalable cloud solution",
                                                        List.of(
                                                                new CVProject.Achievement("Reduced transaction failures by 95%", List.of("Reliability")),
                                                                new CVProject.Achievement("Achieved PCI DSS compliance", List.of("Security"))
                                                        ),
                                                        List.of("Java", "AWS", "PCI Compliance")
                                                )
                                        )
                                ),
                                new CVProject.Position(
                                        "Senior Developer",
                                        new CVProject.Period(LocalDate.of(2019, 6, 1), LocalDate.of(2022, 2, 28)),
                                        "Full-stack development for SaaS platform",
                                        List.of(
                                                "Implemented CI/CD pipeline",
                                                "Optimized database performance",
                                                "Developed React frontend components"
                                        ),
                                        List.of(
                                                new CVProject.Project(
                                                        "User Analytics Dashboard",
                                                        "Created real-time data visualization platform",
                                                        List.of(
                                                                new CVProject.Achievement("Improved query performance by 300%", List.of("Optimization")),
                                                                new CVProject.Achievement("Reduced client-side load time by 60%", List.of("UX"))
                                                        ),
                                                        List.of("React", "GraphQL", "D3.js")
                                                )
                                        )
                                )
                        )
                ),

                new CVProject.Employer(
                        "FinSystems Inc",
                        "New York, NY, USA",
                        new CVProject.Period(LocalDate.of(2016, 7, 15), LocalDate.of(2019, 5, 15)),
                        List.of(
                                new CVProject.Position(
                                        "Software Engineer",
                                        new CVProject.Period(LocalDate.of(2016, 7, 15), LocalDate.of(2019, 5, 15)),
                                        "Backend development for financial systems",
                                        List.of(
                                                "Developed REST APIs for trading platform",
                                                "Implemented fraud detection algorithms",
                                                "Performed system security audits"
                                        ),
                                        List.of(
                                                new CVProject.Project(
                                                        "Risk Analysis Engine",
                                                        "Real-time market risk calculation system",
                                                        List.of(
                                                                new CVProject.Achievement("Handled 10K+ transactions/sec", List.of("Scalability")),
                                                                new CVProject.Achievement("Reduced false positives by 40%", List.of("Accuracy"))
                                                        ),
                                                        List.of("Java", "Kafka", "Machine Learning")
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static List<CVProject.Education> generateEducations() {
        return List.of(
                new CVProject.Education(
                        "MSc Advanced Computer Science",
                        "Imperial College London",
                        new CVProject.Period(LocalDate.of(2015, 9, 20), LocalDate.of(2016, 11, 15))
                ),
                new CVProject.Education(
                        "BSc Computer Science",
                        "University of Manchester",
                        new CVProject.Period(LocalDate.of(2012, 9, 15), LocalDate.of(2015, 6, 30))
                )
        );
    }
}

