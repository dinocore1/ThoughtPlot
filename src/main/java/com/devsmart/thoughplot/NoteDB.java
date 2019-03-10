package com.devsmart.thoughplot;

import java.io.IOException;

public interface NoteDB {

    Note getNote(String id) throws IOException;

}
