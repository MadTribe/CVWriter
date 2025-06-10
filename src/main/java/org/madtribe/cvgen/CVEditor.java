package org.madtribe.cvgen;
import org.madtribe.cvgen.model.CVProject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.util.Arrays.asList;

public class CVEditor {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final String PERSONAL_INFO_OPT = "1";
    public static final String EMPLOYERS_OPT = "2";
    public static final String ADD_OPT = "A";
    public static final String BACK_OPT = "B";
    public static final String PROJECTS_OPT = "5";
    private static final String DELETE_OPT = "D";
    private CVProject cv;
    private final Scanner scanner = new Scanner(System.in);

    public CVEditor(CVProject initialCV) {
        this.cv = initialCV;
    }

    public void start() {
        mainMenu();
    }

    protected void println(String msg) {
        System.out.println(msg);
    }

    protected void print(String msg) {
        System.out.print(msg);
    }


    protected String getInput() {
        return scanner.nextLine();
    }

    private void mainMenu() {
        while (true) {
            println("\n=== Main Menu ===");
            println(PERSONAL_INFO_OPT + ". Personal Information");
            println(EMPLOYERS_OPT + ". Employers");
            println("3. Education");
            println("4. Professional Summary");
            println("5. Key Achievements");
            println("6. Technical Skills");
            println("7. Spoken Languages");
            println("P. View Complete CV");
            println("X. Exit and Save");
            print("Select option: ");

            switch (getInput()) {
                case PERSONAL_INFO_OPT:
                    contactMenu();
                    break;
                case EMPLOYERS_OPT:
                    employersMenu();
                    break;
                case "3":
                    educationMenu();
                    break;
                case "4":
                    editProfessionalSummary();
                    break;
                case "5":
                    keyAchievementsMenu();
                    break;
                case "6":
                    technicalSkillsMenu();
                    break;
                case "7":
                    spokenLanguagesMenu();
                    break;
                case "P":
                    printCV();
                    break;
                case "X":
                    return;
                default:
                    println("Invalid option");
            }
        }
    }

    // Contact Information Menu
    private void contactMenu() {
        while (true) {
            CVProject.Contact contact = cv.contact();
            println("\n--- Contact Information ---");
            println("1. Full Name: " + cv.fullName());
            println("2. Phone: " + contact.phoneNumber());
            println("3. Email: " + contact.email());
            println("4. LinkedIn: " + contact.linkedIn());
            println("B. Back to Main");
            print("Select option: ");

            switch (getInput()) {
                case "1":
                    print("Enter new full name: ");
                    cv = cv.withFullName(getInput());
                    break;
                case "2":
                    cv = cv.withContact(contact.withPhoneNumber(getInput()));
                    break;
                case "3":
                    cv = cv.withContact(contact.withEmail(getInput()));
                    break;
                case "4":
                    cv = cv.withContact(contact.withLinkedIn(getInput()));
                    break;
                case "B":
                    return;
                default:
                    println("Invalid option");
            }
        }
    }

    // Employers Management Menu
    private void employersMenu() {
        while (true) {
            println("\n--- Employers Management ---");
            List<CVProject.Employer> employers = cv.employers();
            for (int i = 0; i < employers.size(); i++) {
                println((i + 1) + ". " + employers.get(i).name());
            }
            println(ADD_OPT + ". Add Employer");
            println(BACK_OPT + ". Back to Main");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase(ADD_OPT)) {
                addEmployer();
            } else if (input.equalsIgnoreCase(BACK_OPT)) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < employers.size()) {
                        employerMenu(index);
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    private void addEmployer() {
        print("Employer name: ");
        String name = getInput();
        print("Location: ");
        String location = getInput();
        CVProject.Period period = editPeriod(null);

        List<CVProject.Position> positions = new ArrayList<>();
        cv = cv.withEmployers(appendToList(
                cv.employers(),
                new CVProject.Employer(name, location, period, positions)
        ));
    }

    private void employerMenu(int employerIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        while (true) {
            println("\n--- Employer: " + employer.name() + " ---");
            println("1. Name: " + employer.name());
            println("2. Location: " + employer.location());
            println("3. Period: " + formatPeriod(employer.period()));
            println("4. Positions");
            println("D. Delete Employer");
            println(BACK_OPT + ". Back to Employers");
            print("Select option: ");

            switch (getInput()) {
                case "1":
                    print("Enter new name: ");
                    employer = employer.withName(getInput());
                    updateEmployer(employerIndex, employer);
                    break;
                case "2":
                    print("Enter new location: ");
                    employer = employer.withLocation(getInput());
                    updateEmployer(employerIndex, employer);
                    break;
                case "3":
                    employer = employer.withPeriod(editPeriod(employer.period()));
                    updateEmployer(employerIndex, employer);
                    break;
                case "4":
                    positionsMenu(employerIndex);
                    break;
                case "D":
                    deleteEmployer(employerIndex);
                    return;
                case BACK_OPT:
                    return;
                default:
                    println("Invalid option");
            }
        }
    }

    private void positionsMenu(int employerIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        List<CVProject.Position> positions = employer.positions();

        while (true) {
            println("\n--- Positions at " + employer.name() + " ---");
            for (int i = 0; i < positions.size(); i++) {
                println((i + 1) + ". " + positions.get(i).title());
            }
            println("A. Add Position");
            println("B. Back to Employer");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                addPosition(employerIndex);
                positions = cv.employers().get(employerIndex).positions(); // Refresh
            } else if (input.equalsIgnoreCase("B")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < positions.size()) {
                        positionMenu(employerIndex, index);
                        positions = cv.employers().get(employerIndex).positions(); // Refresh
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    private void addPosition(int employerIndex) {
        print("Position title: ");
        String title = getInput();
        CVProject.Period period = editPeriod(null);
        print("Description: ");
        String description = getInput();

        List<String> responsibilities = new ArrayList<>();
        List<CVProject.Project> projects = new ArrayList<>();

        CVProject.Position newPosition = new CVProject.Position(
                title, period, description, responsibilities, projects
        );

        CVProject.Employer employer = cv.employers().get(employerIndex);
        List<CVProject.Position> updatedPositions = appendToList(
                employer.positions(), newPosition
        );

        cv = cv.withEmployers(updateInList(
                cv.employers(), employerIndex,
                employer.withPositions(updatedPositions)
        ));
    }

    private void positionMenu(int employerIndex, int positionIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);

        while (true) {
            println("\n--- Position: " + position.title() + " ---");
            println("1. Title: " + position.title());
            println("2. Period: " + formatPeriod(position.period()));
            println("3. Description: " + position.description());
            println("4. Responsibilities");
            println(PROJECTS_OPT + ". Projects");
            println(DELETE_OPT + ". Delete Position");
            println(BACK_OPT + ". Back to Positions");
            print("Select option: ");

            switch (getInput()) {
                case "1":
                    print("Enter new title: ");
                    position = position.withTitle(getInput());
                    updatePosition(employerIndex, positionIndex, position);
                    break;
                case "2":
                    position = position.withPeriod(editPeriod(position.period()));
                    updatePosition(employerIndex, positionIndex, position);
                    break;
                case "3":
                    print("Enter new description: ");
                    position = position.withDescription(getInput());
                    updatePosition(employerIndex, positionIndex, position);
                    break;
                case "4":
                    responsibilitiesMenu(employerIndex, positionIndex);
                    break;
                case PROJECTS_OPT:
                    projectsMenu(employerIndex, positionIndex);
                    break;
                case DELETE_OPT:
                    deletePosition(employerIndex, positionIndex);
                    return;
                case BACK_OPT:
                    return;
                default:
                    println("Invalid option");
            }
        }
    }

    private void responsibilitiesMenu(int employerIndex, int positionIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);
        List<String> responsibilities = position.responsibilities();

        while (true) {
            println("\n--- Responsibilities ---");
            for (int i = 0; i < responsibilities.size(); i++) {
                println((i + 1) + ". " + responsibilities.get(i));
            }
            println("A. Add Responsibility");
            println("B. Back to Position");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                print("Enter responsibility: ");
                responsibilities = appendToList(responsibilities, getInput());
                updatePosition(employerIndex, positionIndex, position.withResponsibilities(responsibilities));
            } else if (input.equalsIgnoreCase("B")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < responsibilities.size()) {
                        println("1. Edit  2. Delete");
                        String choice = getInput();
                        if (choice.equals("1")) {
                            print("New text: ");
                            responsibilities = updateInList(
                                    responsibilities, index, getInput()
                            );
                            updatePosition(employerIndex, positionIndex, position.withResponsibilities(responsibilities));
                        } else if (choice.equals("2")) {
                            responsibilities = removeFromList(responsibilities, index);
                            updatePosition(employerIndex, positionIndex, position.withResponsibilities(responsibilities));
                        }
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    private void projectsMenu(int employerIndex, int positionIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);
        List<CVProject.Project> projects = position.projects();

        while (true) {
            println("\n--- Projects ---");
            for (int i = 0; i < projects.size(); i++) {
                println((i + 1) + ". " + projects.get(i).title());
            }
            println("A. Add Project");
            println("B. Back to Position");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                addProject(employerIndex, positionIndex);
                projects = cv.employers().get(employerIndex).positions().get(positionIndex).projects(); // Refresh
            } else if (input.equalsIgnoreCase("B")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < projects.size()) {
                        projectMenu(employerIndex, positionIndex, index);
                        projects = cv.employers().get(employerIndex).positions().get(positionIndex).projects(); // Refresh
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    private List<String> chooseTags(List<String> tags){

        boolean loop = true;

        List<String> ret = new ArrayList<String>();

        while (loop) {
            var oldTags = new ArrayList<>(cv.tags());
            println("Choose Tags: ");
            int idx = 1;

            for (String t : oldTags) {
                println(idx + " - " + t);
                idx++;
            }
            println("A - Add Tag" );
            println("S - Save" );

            String choice = getInput();
            try {

                int selectedIdx = Integer.parseInt(choice);
                if (selectedIdx < oldTags.size()){
                    ret.add(oldTags.get(selectedIdx - 1));
                }
            } catch (Exception ignored) {
            }

            switch (choice) {
                case "A":
                    var newTag = getInput();
                    ret.add(newTag);
                    oldTags.add(newTag);
                    cv = cv.withTags(oldTags);
                    break;
                case "S":
                    loop = false;
                    break;
                default:
                    println("Unknown Option");
            }

        }
        return ret;

    }

    private void addProject(int employerIndex, int positionIndex) {
        print("Project title: ");
        String title = getInput();
        print("Description: ");
        String description = getInput();

        List<CVProject.Achievement> achievements = new ArrayList<>();
        List<String> tags = chooseTags(new ArrayList<>());

        CVProject.Project newProject = new CVProject.Project(
                title, description, achievements, tags
        );

        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);
        List<CVProject.Project> updatedProjects = appendToList(
                position.projects(), newProject
        );

        updatePosition(employerIndex, positionIndex, position.withProjects(updatedProjects));
    }

    private void projectMenu(int employerIndex, int positionIndex, int projectIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);
        CVProject.Project project = position.projects().get(projectIndex);

        while (true) {
            println("\n--- Project: " + project.title() + " ---");
            println("1. Title: " + project.title());
            println("2. Description: " + project.description());
            println("3. Achievements");
            println("4. Tags");
            println("5. Delete Project");
            println("6. Back to Projects");
            print("Select option: ");

            switch (getInput()) {
                case "1":
                    print("Enter new title: ");
                    project = project.withTitle(getInput());
                    updateProject(employerIndex, positionIndex, projectIndex, project);
                    break;
                case "2":
                    print("Enter new description: ");
                    project = project.withDescription(getInput());
                    updateProject(employerIndex, positionIndex, projectIndex, project);
                    break;
                case "3":
                    achievementsMenu(employerIndex, positionIndex, projectIndex);
                    break;
                case "4":
                    tagsMenu(employerIndex, positionIndex, projectIndex);
                    break;
                case "5":
                    deleteProject(employerIndex, positionIndex, projectIndex);
                    return;
                case "6":
                    return;
                default:
                    println("Invalid option");
            }
        }
    }

    private void achievementsMenu(int employerIndex, int positionIndex, int projectIndex) {
        // Similar implementation to responsibilitiesMenu
        // Full implementation in final code
    }

    private void tagsMenu(int employerIndex, int positionIndex, int projectIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);
        CVProject.Project project = position.projects().get(projectIndex);

        println("\n--- Tags for Project " + project.title() + "---");
        List<String> tags = chooseTags(project.tags());

        project = project.withTags(tags);

        position = position.withProjects(updateInList(position.projects(),projectIndex, project ));
        employer = employer.withPositions(updateInList(employer.positions(),positionIndex,position));


    }

    // Education Section
    private void educationMenu() {
        while (true) {
            println("\n--- Education ---");
            List<CVProject.Education> educations = cv.educations();
            for (int i = 0; i < educations.size(); i++) {
                println((i + 1) + ". " + educations.get(i).title());
            }
            println("A. Add Education");
            println("B. Back to Main");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                addEducation();
            } else if (input.equalsIgnoreCase("B")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < educations.size()) {
                        educationItemMenu(index);
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    private void addEducation() {
        print("Degree/Title: ");
        String title = getInput();
        print("Institution: ");
        String institution = getInput();
        CVProject.Period period = editPeriod(null);

        cv = cv.withEducations(appendToList(
                cv.educations(),
                new CVProject.Education(title, institution, period)
        ));
    }

    private void educationItemMenu(int index) {
        CVProject.Education education = cv.educations().get(index);
        while (true) {
            println("\n--- Education: " + education.title() + " ---");
            println("1. Title: " + education.title());
            println("2. Institution: " + education.institution());
            println("3. Period: " + formatPeriod(education.period()));
            println("4. Delete Education");
            println("5. Back to Education");
            print("Select option: ");

            switch (getInput()) {
                case "1":
                    print("New title: ");
                    education = education.withTitle(getInput());
                    updateEducation(index, education);
                    break;
                case "2":
                    print("New institution: ");
                    education = education.withInstitution(getInput());
                    updateEducation(index, education);
                    break;
                case "3":
                    education = education.withPeriod(editPeriod(education.period()));
                    updateEducation(index, education);
                    break;
                case "4":
                    deleteEducation(index);
                    return;
                case "5":
                    return;
                default:
                    println("Invalid option");
            }
        }
    }

    // Professional Summary
    private void editProfessionalSummary() {
        println("\n--- Professional Summary ---");
        println("Current summary:");
        println(cv.professionalSummary());
        println("Enter new summary (type 'END' on a new line to finish):");

        StringBuilder sb = new StringBuilder();
        String line;
        while (!(line = getInput()).equals("END")) {
            sb.append(line).append("\n");
        }

        cv = cv.withProfessionalSummary(sb.toString().trim());
    }

    private CVProject.Achievement createAchievement(){

        return null;
    }

    // Key Achievements
    private void keyAchievementsMenu() {
        List<CVProject.Achievement> achievements = cv.keyAchievements();
        while (true) {
            println("\n--- Key Achievements ---");
            for (int i = 0; i < achievements.size(); i++) {
                println((i + 1) + ". " + achievements.get(i));
            }
            println("A. Add Achievement");
            println("B. Back to Main");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                print("Enter achievement: ");
                achievements = appendToList(achievements,  createAchievement());
                cv = cv.withKeyAchievements(achievements);
            } else if (input.equalsIgnoreCase("B")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < achievements.size()) {
                        println("1. Edit  2. Delete");
                        String choice = getInput();
                        if (choice.equals("1")) {
                            print("New text: ");
                            achievements = updateInList(achievements, index, new CVProject.Achievement(getInput(), asList()));
                            cv = cv.withKeyAchievements(achievements);
                        } else if (choice.equals("2")) {
                            achievements = removeFromList(achievements, index);
                            cv = cv.withKeyAchievements(achievements);
                        }
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    // Technical Skills
    private void technicalSkillsMenu() {
        Map<String, List<CVProject.Skill>> skills = cv.technicalSkills();
        while (true) {
            println("\n--- Technical Skills ---");
            List<String> categories = new ArrayList<>(skills.keySet());
            for (int i = 0; i < categories.size(); i++) {
                println((i + 1) + ". " + categories.get(i));
            }
            println("A. Add Category");
            println("B. Back to Main");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                print("Category name: ");
                String category = getInput();
                skills = new HashMap<>(skills);
                skills.put(category, new ArrayList<>());
                cv = cv.withTechnicalSkills(skills);
            } else if (input.equalsIgnoreCase("B")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < categories.size()) {
                        skillCategoryMenu(categories.get(index));
                        skills = cv.technicalSkills(); // Refresh after changes
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    private void skillCategoryMenu(String category) {
        Map<String, List<CVProject.Skill>> skills = cv.technicalSkills();
        List<CVProject.Skill> categorySkills = skills.get(category);

        while (true) {
            println("\n--- " + category + " Skills ---");
            for (int i = 0; i < categorySkills.size(); i++) {
                println((i + 1) + ". " + categorySkills.get(i).name());
            }
            println("A. Add Skill");
            println("B. Rename Category");
            println("C. Delete Category");
            println("D. Back to Skills");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                print("Skill name: ");
                String skillName = getInput();
                print("Tags (comma separated): ");
                List<String> tags = chooseTags(new ArrayList<>());

                categorySkills = appendToList(categorySkills,
                        new CVProject.Skill(skillName, tags));

                updateSkillCategory(category, categorySkills);
            } else if (input.equalsIgnoreCase("B")) {
                print("New category name: ");
                String newCategory = getInput();

                skills = new HashMap<>(skills);
                skills.remove(category);
                skills.put(newCategory, categorySkills);
                cv = cv.withTechnicalSkills(skills);
                return;
            } else if (input.equalsIgnoreCase("C")) {
                skills = new HashMap<>(skills);
                skills.remove(category);
                cv = cv.withTechnicalSkills(skills);
                return;
            } else if (input.equalsIgnoreCase("D")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < categorySkills.size()) {
                        skillItemMenu(category, index);
                        categorySkills = cv.technicalSkills().get(category); // Refresh
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    private void skillItemMenu(String category, int index) {
        // Implementation similar to other item editors
        // Full implementation in final code

        while (true){
            println(this.cv.technicalSkills().get(category).get(index).name());
            println("B. back");

            String input = getInput().toUpperCase();
            if (input.equalsIgnoreCase("B")) {
                break;
            }

            if (input.equalsIgnoreCase("D")) {
                var techSkills = cv.technicalSkills();
                var skills = removeFromList(this.cv.technicalSkills().get(category),index);

                techSkills.put(category, skills);

                cv = cv.withTechnicalSkills(techSkills);
            }

        }
    }

    // Spoken Languages
    private void spokenLanguagesMenu() {
        List<String> languages = cv.spokenLanguages();
        while (true) {
            println("\n--- Spoken Languages ---");
            for (int i = 0; i < languages.size(); i++) {
                println((i + 1) + ". " + languages.get(i));
            }
            println("A. Add Language");
            println("B. Back to Main");
            print("Select option: ");

            String input = getInput();
            if (input.equalsIgnoreCase("A")) {
                print("Language: ");
                languages = appendToList(languages, getInput());
                cv = cv.withSpokenLanguages(languages);
            } else if (input.equalsIgnoreCase("B")) {
                return;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < languages.size()) {
                        println("1. Edit  2. Delete");
                        String choice = getInput();
                        if (choice.equals("1")) {
                            print("New language: ");
                            languages = updateInList(languages, index, getInput());
                            cv = cv.withSpokenLanguages(languages);
                        } else if (choice.equals("2")) {
                            languages = removeFromList(languages, index);
                            cv = cv.withSpokenLanguages(languages);
                        }
                    } else {
                        println("Invalid index");
                    }
                } catch (NumberFormatException e) {
                    println("Invalid option");
                }
            }
        }
    }

    // Helper methods
    private CVProject.Period editPeriod(CVProject.Period current) {
        LocalDate from = parseDate("Start date");
        LocalDate to = null;
        print("End date (leave blank if current, YYYY-MM-DD): ");
        String toInput = getInput();
        if (!toInput.isBlank()) {
            to = LocalDate.parse(toInput, DATE_FORMAT);
        } else {
            to = LocalDate.now();
        }
        return new CVProject.Period(from, to);
    }

    private LocalDate parseDate(String prompt) {
        while (true) {
            print(prompt + " (YYYY-MM-DD): ");
            try {
                return LocalDate.parse(getInput(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private String formatPeriod(CVProject.Period period) {
        return period.from() + " to " + (period.to() != null ? period.to() : "Present");
    }

    private <T> List<T> appendToList(List<T> list, T item) {
        List<T> newList = new ArrayList<>(list);
        newList.add(item);
        return newList;
    }

    private <T> List<T> updateInList(List<T> list, int index, T item) {
        List<T> newList = new ArrayList<>(list);
        newList.set(index, item);
        return newList;
    }

    private <T> List<T> removeFromList(List<T> list, int index) {
        List<T> newList = new ArrayList<>(list);
        newList.remove(index);
        return newList;
    }

    private void updateEmployer(int index, CVProject.Employer employer) {
        List<CVProject.Employer> employers = updateInList(cv.employers(), index, employer);
        cv = cv.withEmployers(employers);
    }

    private void updatePosition(int employerIndex, int positionIndex, CVProject.Position position) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        List<CVProject.Position> positions = updateInList(
                employer.positions(), positionIndex, position
        );
        updateEmployer(employerIndex, employer.withPositions(positions));
    }

    private void updateProject(int employerIndex, int positionIndex, int projectIndex, CVProject.Project project) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);
        List<CVProject.Project> projects = updateInList(
                position.projects(), projectIndex, project
        );
        updatePosition(employerIndex, positionIndex, position.withProjects(projects));
    }

    private void updateEducation(int index, CVProject.Education education) {
        List<CVProject.Education> educations = updateInList(cv.educations(), index, education);
        cv = cv.withEducations(educations);
    }

    private void updateSkillCategory(String category, List<CVProject.Skill> skills) {
        Map<String, List<CVProject.Skill>> skillMap = new HashMap<>(cv.technicalSkills());
        skillMap.put(category, skills);
        cv = cv.withTechnicalSkills(skillMap);
    }

    private void deleteEmployer(int index) {
        List<CVProject.Employer> employers = removeFromList(cv.employers(), index);
        cv = cv.withEmployers(employers);
    }

    private void deletePosition(int employerIndex, int positionIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        List<CVProject.Position> positions = removeFromList(employer.positions(), positionIndex);
        updateEmployer(employerIndex, employer.withPositions(positions));
    }

    private void deleteProject(int employerIndex, int positionIndex, int projectIndex) {
        CVProject.Employer employer = cv.employers().get(employerIndex);
        CVProject.Position position = employer.positions().get(positionIndex);
        List<CVProject.Project> projects = removeFromList(position.projects(), projectIndex);
        updatePosition(employerIndex, positionIndex, position.withProjects(projects));
    }

    private void deleteEducation(int index) {
        List<CVProject.Education> educations = removeFromList(cv.educations(), index);
        cv = cv.withEducations(educations);
    }

    private void printCV() {
        // Implementation to pretty-print the entire CV
        // Would display all sections in readable format
        println("\n=== COMPLETE CV ===");
        println("Name: " + cv.fullName());
        println("Contact: " + cv.contact());
        // ... rest of printing logic
    }

    public CVProject getCV() {
        return cv;
    }


}