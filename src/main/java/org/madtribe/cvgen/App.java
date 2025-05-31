package org.madtribe.cvgen;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App implements Runnable{

    @Option(names = {"-f", "--file"}, description = "project file", required = true)
    private String file;

    @Option(names = {"-v", "--verbose"}, description = "Verbose mode")
    private boolean verbose;

    @Option(names = {"-i", "--initialise"}, description = "Initialize")
    private boolean init;

    @Option(names = {"-n", "--fullName"}, description = "Full Name of CV owner")
    private String fullName = "empty";


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
            if (init) {
                new ProjectFileGenerator().init(fullName,file);
            }
        } catch (IOException e) {
            System.err.println("An Error Happened");
            e.printStackTrace();
        }
    }

}