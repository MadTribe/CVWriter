package org.madtribe.cvgen;
import freemarker.template.TemplateException;
import org.madtribe.cvgen.model.CVProject;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class App implements Runnable{

    @Option(names = {"-f", "--file"}, description = "project file")
    private String file;

    @Option(names = {"-F", "--folder"}, description = "project folder")
    private String folder;

    @Option(names = {"-v", "--verbose"}, description = "Verbose mode")
    private boolean verbose;

    @Option(names = {"-e", "--edit"}, description = "Edit CV")
    private boolean edit;

    @Option(names = {"-i", "--initialise"}, description = "Initialize")
    private boolean init;

    @Option(names = {"-l", "--list-tags"}, description = "List Tags")
    private boolean listTags;

    @Option(names = {"-q", "--filter-tags"}, description = "Filter on Tags comma separated")
    private String filterTags = "";


    @Option(names = {"-n", "--fullName"}, description = "Full Name of CV owner")
    private String fullName = "empty";

    @Option(names = {"-t", "--templateName"}, description = "Freemarker Template to use in templates folder. Defaults to cv_default_template.md")
    private String templateName = "cv_default_template.md";

    @Option(names = {"-o", "--output"}, description = "Output file to create")
    private String outputFile;


    public static void main(String[] args) {
        System.err.println("CV Generator");
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.printf("Project File, %s!%n", file);
        if (verbose) {
            System.out.println("Verbose mode is on.");
        }

        try {
            CVProject project = null;
            if (init) {
                project = new ProjectFileGenerator().init(fullName, file);
            }

            if (file != null) {
                project = new ProjectFileLoader().load(file);
            }

            if (folder != null) {
                project = CVProjectExporterImporter.importCV(Path.of(folder));
            }


            if (edit) {
                var editor = new CVEditor(project);
                editor.start();
                project = editor.getCV();
                if (file != null) {
                    ProjectFileGenerator.writeUsingFiles(Utils.addTimestampToFileName(file), true, new ProjectFileGenerator().serializeProject(project));
                }

                if (folder != null) {
                    Utils.copyFolder(Path.of(folder),Path.of(Utils.addTimestampToFileName(folder)+ "bk"));
                    CVProjectExporterImporter.export(project, Path.of(folder));
                }

            }

            if (listTags) {
                System.out.println(project.tags());
            }

            if (project != null ) {

                if (!filterTags.equalsIgnoreCase("")) {
                    project = CVFilter.filter(project, Arrays.asList(filterTags.split(",")));
                }
                project = CVFilter.sortByDateDescending(project);
            }

            if (outputFile != null && project != null ) {
                CVRenderer renderer = new CVRenderer();
                try (var fileWriter = new FileWriter(outputFile)) {
                    renderer.renderCV(project, templateName, fileWriter);
                }
            }

        } catch (IOException | TemplateException e) {
            System.err.printf("An Error Happened: %s", e.getMessage() );

            if (verbose){
                e.printStackTrace();
            }
        }
    }

}