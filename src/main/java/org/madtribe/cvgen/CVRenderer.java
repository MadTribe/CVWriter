package org.madtribe.cvgen;

import freemarker.core.HTMLOutputFormat;
import freemarker.core.PlainTextOutputFormat;
import freemarker.template.*;
import org.madtribe.cvgen.model.CVProject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class CVRenderer {
    public void renderCV(CVProject cv, String templateName, FileWriter out) throws IOException, TemplateException {
        Configuration cfg = getFreemarkerConfig(false);
        // Process template
        Template template = cfg.getTemplate(templateName);

        template.process(Map.of("cv", cv), out);

        System.out.println("CV generated successfully!");
    }

    private static Configuration getFreemarkerConfig(boolean isHtml) throws IOException, TemplateModelException {
        // Configure FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("templates"));
        cfg.setDefaultEncoding("UTF-8");

        if (isHtml) {
            cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
        } else {
            cfg.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        }
        // Add date formatter to configuration
        cfg.setSharedVariable("dateFmt", new SimpleDateFormat("MMM yyyy"));
        return cfg;
    }

}