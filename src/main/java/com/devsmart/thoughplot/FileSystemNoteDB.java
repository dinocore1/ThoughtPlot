package com.devsmart.thoughplot;

import com.google.common.base.Charsets;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystemNoteDB implements NoteDB {

    public static final Pattern REGEX = Pattern.compile("([[a-zA-Z_0-9] ]+).md$");

    private final File rootDir;

    public FileSystemNoteDB(File rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public Note getNote(String id) throws IOException {
        Note retval = null;
        File[] files = rootDir.listFiles();
        if(files != null) {
            for(File f : files) {
                String filename = f.getName();
                Matcher m = REGEX.matcher(filename);
                if(m.find()) {
                    if(m.group(1).equalsIgnoreCase(id)) {
                        retval = new Note(id);
                        retval.markdown = Files.asByteSource(f).asCharSource(Charsets.UTF_8).read();
                        break;
                    }
                }
            }
        }
        return retval;
    }

    @Override
    public void save(Note note) throws IOException {
        File f = new File(rootDir, String.format("%s.md", note.name));
        Files.asByteSink(f).asCharSink(Charsets.UTF_8).write(note.markdown);

    }
}
