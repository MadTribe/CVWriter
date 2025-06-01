package org.madtribe.cvgen;
import freemarker.template.TemplateException;
import org.madtribe.cvgen.model.CVProject;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.FileWriter;
import java.io.IOException;

public class App implements Runnable{

    @Option(names = {"-f", "--file"}, description = "project file", required = true)
    private String file;

    @Option(names = {"-v", "--verbose"}, description = "Verbose mode")
    private boolean verbose;

    @Option(names = {"-i", "--initialise"}, description = "Initialize")
    private boolean init;

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