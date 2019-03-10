package com.devsmart.thoughplot;

import org.junit.Test;

import static org.junit.Assert.*;

public class ViewEngineTests {

    @Test
    public void testLinkVisitor() {
        Note note = new Note();
        note.markdown = "this doc contains links to [[google]]";

        ViewEngine engine = new ViewEngine();
        engine.processNote(note);

        assertEquals(1, note.linkRefs.size());
    }
}
