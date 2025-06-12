package org.madtribe.cvgen;

import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.madtribe.cvgen.model.CVProject;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class CVProjectExporterImporter {

    private static final Yaml yaml;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_DATE;

    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml  = new Yaml(new RecordRepresenter(options) );
    }

    // Export CVProject to folder structure
    public static void export(CVProject cv, Path rootDir) throws IOException {
        Files.createDirectories(rootDir);
        writeRootDetails(cv, rootDir);
        exportEmployers(cv, rootDir);
        exportEducations(cv, rootDir);
        exportAchievements(cv, rootDir);
        exportTechnicalSkills(cv, rootDir);
    }
    static class RecordRepresenter extends Representer {
        public RecordRepresenter(org.yaml.snakeyaml.DumperOptions options) {
            super(options);
            this.multiRepresenters.put(Record.class, new RepresentRecord());
            // Handle LocalDate
            this.representers.put(LocalDate.class, new RepresentLocalDate());
        }

        class RepresentRecord extends RepresentMap {
            @Override
            public Node representData(Object data) {
                Map<String, Object> fields = new LinkedHashMap<>();
                for (Field field : data.getClass().getDeclaredFields()) {
                    try {
                        field.setAccessible(true);
                        fields.put(field.getName(), field.get(data));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                return super.representData(fields);
            }
        }

        class RepresentLocalDate implements Represent {
            @Override
            public Node representData(Object data) {
                LocalDate date = (LocalDate) data;
                return representScalar(Tag.STR, DATE_FORMAT.format(date));
            }
        }
    }

    private static void writeRootDetails(CVProject cv, Path rootDir) throws IOException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("fullName", cv.fullName());
        data.put("contact", cv.contact());
        data.put("professionalSummary", cv.professionalSummary());
        data.put("spokenLanguages", cv.spokenLanguages());
        data.put("tags", cv.tags());
        writeYamlFile(rootDir.resolve("details.md"), data, "");
    }

    private static void exportEmployers(CVProject cv, Path rootDir) throws IOException {
        Path employersDir = rootDir.resolve("employers");
        Files.createDirectories(employersDir);
        List<String> employerDirs = new ArrayList<>();

        for (int i = 0; i < cv.employers().size(); i++) {
            CVProject.Employer emp = cv.employers().get(i);
            String dirName = "employer_" + i;
            Path empDir = employersDir.resolve(dirName);
            Files.createDirectories(empDir);
            employerDirs.add(dirName);

            // Write employer details
            Map<String, Object> empData = new LinkedHashMap<>();
            empData.put("name", emp.name());
            empData.put("location", emp.location());
            empData.put("period", emp.period());
            writeYamlFile(empDir.resolve("details.md"), empData,"");

            // Export positions
            Path positionsDir = empDir.resolve("positions");
            Files.createDirectories(positionsDir);
            List<String> positionDirs = new ArrayList<>();

            for (int j = 0; j < emp.positions().size(); j++) {
                CVProject.Position pos = emp.positions().get(j);
                String posDirName = "position_" + j;
                Path posDir = positionsDir.resolve(posDirName);
                Files.createDirectories(posDir);
                positionDirs.add(posDirName);

                // Write position details
                Map<String, Object> posData = new LinkedHashMap<>();
                posData.put("title", pos.title());
                posData.put("period", pos.period());
                posData.put("responsibilities", pos.responsibilities());
                writeYamlFile(posDir.resolve("details.md"), posData,pos.description());

                // Export projects
                Path projectsDir = posDir.resolve("projects");
                Files.createDirectories(projectsDir);
                List<String> projectFiles = new ArrayList<>();

                for (int k = 0; k < pos.projects().size(); k++) {
                    CVProject.Project proj = pos.projects().get(k);
                    String projFileName = "project_" + k + ".md";
                    Map<String, Object> projData = new LinkedHashMap<>();
                    projData.put("title", proj.title());
                    projData.put("achievements", proj.achievements());
                    projData.put("tags", proj.tags());
                    writeYamlFile(projectsDir.resolve(projFileName), projData, proj.description());
                    projectFiles.add(projFileName);
                }

                // Write projects index
                writeIndexFile(projectsDir, projectFiles);
            }

            // Write positions index
            writeIndexFile(positionsDir, positionDirs);
        }

        // Write employers index
        writeIndexFile(employersDir, employerDirs);
    }

    private static void exportEducations(CVProject cv, Path rootDir) throws IOException {
        Path educationsDir = rootDir.resolve("educations");
        Files.createDirectories(educationsDir);
        List<String> educationFiles = new ArrayList<>();

        for (int i = 0; i < cv.educations().size(); i++) {
            CVProject.Education edu = cv.educations().get(i);
            String fileName = "education_" + i + ".md";
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("title", edu.title());
            data.put("institution", edu.institution());
            data.put("period", edu.period());
            writeYamlFile(educationsDir.resolve(fileName), data,"");
            educationFiles.add(fileName);
        }

        writeIndexFile(educationsDir, educationFiles);
    }

    private static void exportAchievements(CVProject cv, Path rootDir) throws IOException {
        Path achievementsDir = rootDir.resolve("achievements");
        Files.createDirectories(achievementsDir);
        List<String> achievementFiles = new ArrayList<>();

        for (int i = 0; i < cv.keyAchievements().size(); i++) {
            CVProject.Achievement ach = cv.keyAchievements().get(i);
            String fileName = "achievement_" + i + ".md";
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("tags", ach.tags());
            writeYamlFile(achievementsDir.resolve(fileName), data,ach.name());
            achievementFiles.add(fileName);
        }

        writeIndexFile(achievementsDir, achievementFiles);
    }

    private static void exportTechnicalSkills(CVProject cv, Path rootDir) throws IOException {
        Path techSkillsDir = rootDir.resolve("technicalSkills");
        Files.createDirectories(techSkillsDir);

        for (Map.Entry<String, List<CVProject.Skill>> entry : cv.technicalSkills().entrySet()) {
            String category = entry.getKey();
            String fileName = sanitizeFilename(category) + ".md";
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("category", category);
            data.put("skills", entry.getValue());
            writeYamlFile(techSkillsDir.resolve(fileName), data,"");
        }
    }

    private static void writeYamlFile(Path file, Object data, String markdownBody) throws IOException {
        String yamlStr = yaml.dump(data);
        Files.writeString(file, "---\n" + yamlStr + "---\n" + markdownBody, StandardOpenOption.CREATE);
    }

    private static void writeIndexFile(Path dir, List<String> items) throws IOException {
        Map<String, Object> indexData = new LinkedHashMap<>();
        indexData.put("order", items);
        writeYamlFile(dir.resolve("_index.md"), indexData, "");
    }

    private static String sanitizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9]", "_");
    }

    // Import folder structure back to CVProject
    public static CVProject importCV(Path rootDir) throws IOException {
        Map<String, Object> rootData = readYamlFile(rootDir.resolve("details.md"));
        CVProject cv = new CVProject((String) rootData.get("fullName"))
                .withContact(convertMap((Map<String, ?>) rootData.get("contact"), CVProject.Contact.class))
                .withProfessionalSummary((String) rootData.get("professionalSummary"))
                .withSpokenLanguages((List<String>) rootData.get("spokenLanguages"))
                .withTags((List<String>) rootData.get("tags"));

        cv = cv.withProfessionalSummaries(importProfessionalSummary(rootDir.resolve("professionalSummaries")));
        cv = cv.withEmployers(importEmployers(rootDir.resolve("employers")));
        cv = cv.withEducations(importEducations(rootDir.resolve("educations")));
        cv = cv.withKeyAchievements(importAchievements(rootDir.resolve("achievements")));
        cv = cv.withTechnicalSkills(importTechnicalSkills(rootDir.resolve("technicalSkills")));
        return cv;
    }

    private static List<CVProject.Employer> importEmployers(Path employersDir) throws IOException {
        List<String> dirs = getOrderedItems(employersDir);
        List<CVProject.Employer> employers = new ArrayList<>();

        for (String dirName : dirs) {
            Path empDir = employersDir.resolve(dirName);
            Map<String, Object> empData = readYamlFile(empDir.resolve("details.md"));
            List<CVProject.Position> positions = importPositions(empDir.resolve("positions"));
            employers.add(new CVProject.Employer(
                    (String) empData.get("name"),
                    (String) empData.get("location"),
                    convertMap((Map<String, ?>) empData.get("period"), CVProject.Period.class),
                    positions
            ));
        }
        return employers;
    }

    private static List<CVProject.Position> importPositions(Path positionsDir) throws IOException {
        List<String> dirs = getOrderedItems(positionsDir);
        List<CVProject.Position> positions = new ArrayList<>();

        for (String dirName : dirs) {
            Path posDir = positionsDir.resolve(dirName);
            Map<String, Object> posData = readYamlFile(posDir.resolve("details.md"));
            List<CVProject.Project> projects = importProjects(posDir.resolve("projects"));
            positions.add(new CVProject.Position(
                    (String) posData.get("title"),
                    convertMap((Map<String, ?>) posData.get("period"), CVProject.Period.class),
                    (String) posData.get("description"),
                    (List<String>) posData.get("responsibilities"),
                    projects
            ));
        }
        return positions;
    }

    private static List<CVProject.Project> importProjects(Path projectsDir) throws IOException {
        List<String> files = getOrderedItems(projectsDir);
        return files.stream()
                .map(fileName -> {
                    try {
                        Map<String, Object> projData = readYamlFile(projectsDir.resolve(fileName));
                        return new CVProject.Project(
                                (String) projData.get("title"),
                                (String) projData.get("description"),
                                convertList((List<?>) projData.get("achievements"), CVProject.Achievement.class),
                                (List<String>) projData.get("tags")
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<CVProject.Education> importEducations(Path educationsDir) throws IOException {
        return getOrderedItems(educationsDir).stream()
                .map(fileName -> {
                    try {
                        Map<String, Object> data = readYamlFile(educationsDir.resolve(fileName));
                        return new CVProject.Education(
                                (String) data.get("title"),
                                (String) data.get("institution"),
                                convertMap((Map<String, ?>) data.get("period"), CVProject.Period.class)
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<CVProject.Achievement> importAchievements(Path achievementsDir) throws IOException {
        return getOrderedItems(achievementsDir).stream()
                .map(fileName -> {
                    try {
                        Map<String, Object> data = readYamlFile(achievementsDir.resolve(fileName));
                        return new CVProject.Achievement(
                                (String) data.get("name"),
                                (List<String>) data.get("tags")
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static Map<String, List<CVProject.Skill>> importTechnicalSkills(Path techSkillsDir) throws IOException {
        Map<String, List<CVProject.Skill>> skillsMap = new HashMap<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(techSkillsDir, "*.md")) {
            for (Path file : stream) {
                Map<String, Object> data = readYamlFile(file);
                System.out.println("read file " + data);
                String category = (String) data.get("category");
                System.out.println(data.get("skills"));
                List<CVProject.Skill> skills = convertList((List<CVProject.Skill>) data.get("skills"), CVProject.Skill.class);
                skillsMap.put(category, skills);
            }
        }
        return skillsMap;
    }
    private static List<CVProject.ProfessionalSummary> importProfessionalSummary(Path professionalSummaryDir) throws IOException {
        List<CVProject.ProfessionalSummary> skillsMap = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(professionalSummaryDir, "*.md")) {
            for (Path file : stream) {
                Map<String, Object> data = readYamlFile(file);
                List<CVProject.ProfessionalSummary> skills = convertList((List<CVProject.ProfessionalSummary>) data.get("skills"), CVProject.ProfessionalSummary.class);
            }
        }
        return skillsMap;
    }
    private static List<String> getOrderedItems(Path dir) throws IOException {
        Map<String, Object> index = readYamlFile(dir.resolve("_index.md"));
        return (List<String>) index.get("order");
    }

    private static Map<String, Object> readYamlFile(Path file) throws IOException {
        String content = Files.readString(file);
        Pattern pattern = Pattern.compile("^---\\n(.+?)\\n---\\n", Pattern.DOTALL);
        var matcher = pattern.matcher(content);
        if (matcher.find()) {
            String yamlContent = matcher.group(1);
            return yaml.load(yamlContent);
        }
        throw new IOException("Invalid YAML front matter in " + file);
    }


    public static <T> List<T> convertList(List<?> list, Class<T> recordClass) {
        @SuppressWarnings("unchecked")
        var ret = (List<T>) list.stream()
                .map(item -> {
                    System.out.println(item);
                    if (item instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, T> map = (Map<String, T>) item;
                        return convertMap(map, recordClass);
                    }
                    if (item instanceof String) {
                        return item;
                    }

                    throw new IllegalArgumentException("Expected list of maps but found: " + item.getClass());
                })
                .collect(Collectors.toList());
        return ret;
    }

    // Enhanced record conversion with LocalDate support
    private static <T> T convertMap(Map<?, ?> map, Class<T> recordClass) {
        try {
            Constructor<?>[] constructors = recordClass.getDeclaredConstructors();
            Constructor<?> canonicalConstructor = Arrays.stream(constructors)
                    .filter(c -> c.getParameterCount() > 0)
                    .findFirst()
                    .orElseThrow();

            Object[] args = new Object[canonicalConstructor.getParameterCount()];
            Parameter[] params = canonicalConstructor.getParameters();

            for (int i = 0; i < params.length; i++) {
                String paramName = params[i].getName();
                Object value = map.get(paramName);

                if (value == null) {
                    args[i] = null;
                } else if (params[i].getType() == LocalDate.class) {
                    // Convert string to LocalDate
                    args[i] = LocalDate.parse(value.toString(), DATE_FORMAT);
                } else if (value instanceof Map) {
                    // Handle nested records
                    args[i] = convertMap((Map<?, ?>) value, params[i].getType());
                } else if (value instanceof List) {
                    // Handle lists of records or LocalDates
                    args[i] = convertList((List<?>) value, getGenericType(params[i]));
                } else {
                    // Handle basic types
                    args[i] = value;
                }
            }

            return recordClass.cast(canonicalConstructor.newInstance(args));
        } catch (Exception e) {
            throw new RuntimeException("Error creating record: " + recordClass.getSimpleName(), e);
        }
    }

    private static Class<?> getGenericType(Parameter param) {
        Type type = param.getParameterizedType();
        if (type instanceof ParameterizedType) {
            Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                return (Class<?>) typeArgs[0];
            }
        }
        return Object.class;
    }

}