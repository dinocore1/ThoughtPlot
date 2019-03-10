package com.devsmart.thoughplot;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class FilesSystemNoteDBTests {

    @Test
    public void testValidFilename() {
        Matcher m = FileSystemNoteDB.REGEX.matcher("android.md");
        assertTrue(m.find());
        assertEquals("android", m.group(1));
    }
}
