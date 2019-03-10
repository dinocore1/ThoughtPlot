package com.devsmart.thoughplot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("/api/v1")
public class RESTApi {

    @Autowired
    NoteDB noteDB;

    @RequestMapping(path="/note/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getNote(@PathVariable("id") String id) {
        Note note = noteDB.getNote(id);
        return null;
    }

}
