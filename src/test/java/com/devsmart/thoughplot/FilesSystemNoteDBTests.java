package com.devsmart.thoughplot;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class FilesSystemNoteDBTests {

    @Test
    public void testValidFilename() {
        Matcher m = FileSystemNoteDB.REGEX.matcher("docker-compose.md");
        assertTrue(m.find());
        assertEquals("docker-compose", m.group(1));
    }
}
