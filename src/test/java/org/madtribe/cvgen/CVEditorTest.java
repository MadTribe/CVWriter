package org.madtribe.cvgen;

import org.junit.Before;
import org.junit.Test;
import org.madtribe.cvgen.model.CVProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.madtribe.cvgen.CVEditor.*;

public class CVEditorTest {

    private TestableCVEditor instance;


    @Before
    public void setUp() throws Exception {
        instance = new TestableCVEditor(new CVProject("MadTribe").withTags(Arrays.asList("Tag1")));
    }

    @Test
    public void start_then_quit() {
        instance.responses.add(EXIT);
        instance.start();
        assertEquals("MadTribe",instance.getCV().fullName());
    }

    @Test
    public void start_edit_name_quit() {
        instance.responses.add(PERSONAL_INFO_OPT);
        instance.responses.add("1");
        instance.responses.add("Sane Individual");
        instance.responses.add("B");
        instance.responses.add(EXIT);
        instance.start();
        assertEquals("Sane Individual",instance.getCV().fullName());
    }

    @Test
    public void start_add_tag_project() {
        instance.responses.add(EMPLOYERS_OPT);
        instance.responses.add(ADD_OPT);
        instance.responses.add("Acme Software Inc.");
        instance.responses.add("Londinium");
        instance.responses.add("2010-04-01");
        instance.responses.add("2015-04-01");
        instance.responses.add("1");
        instance.responses.add("4");
        instance.responses.add(ADD_OPT);
        instance.responses.add("Chief widget wrangler");
        instance.responses.add("2010-04-01");
        instance.responses.add("2015-04-01");
        instance.responses.add("Wrangled widgets");
        instance.responses.add("1");
        instance.responses.add(PROJECTS_OPT);
        instance.responses.add(ADD_OPT);
        instance.responses.add("Awesome Widget 2001");
        instance.responses.add("An Awesome Widget");
        instance.responses.add(ADD_OPT);
        instance.responses.add("awesomeness");
        instance.responses.add("1");
        instance.responses.add("S");

        instance.responses.add(BACK_OPT);
        instance.responses.add(BACK_OPT);
        instance.responses.add(BACK_OPT);
        instance.responses.add(BACK_OPT); // Back to employer
        instance.responses.add(BACK_OPT); // Back to main
        instance.responses.add(EXIT);

        instance.start();
        assertEquals("MadTribe",instance.getCV().fullName());
        assertEquals("Acme Software Inc.", instance.getCV().employers().get(0).name());
        assertEquals("Londinium", instance.getCV().employers().get(0).location());
        assertEquals("Chief widget wrangler", instance.getCV().employers().get(0).positions().get(0).title());
        assertEquals("Awesome Widget 2001", instance.getCV().employers().get(0).positions().get(0).projects().get(0).title());
        assertEquals("awesomeness", instance.getCV().employers().get(0).positions().get(0).projects().get(0).tags().get(0));
        assertEquals("Tag1", instance.getCV().employers().get(0).positions().get(0).projects().get(0).tags().get(1));
    }

    private class  TestableCVEditor extends  CVEditor {

        public List<String> responses = new ArrayList<>();
        private int nextResponse = 0;

        public TestableCVEditor(CVProject initialCV) {
            super(initialCV);
        }


        @Override
        protected void println(String msg) {
            super.println(msg);
        }

        @Override
        protected void print(String msg) {
            super.print(msg);
        }

        @Override
        protected String getInput() {
            return responses.get(nextResponse++);
        }
    }
}