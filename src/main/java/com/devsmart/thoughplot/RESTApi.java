package com.devsmart.thoughplot;

import com.google.common.collect.Maps;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1")
public class RESTApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTApi.class);

    @Autowired
    NoteDB noteDB;

    @Autowired
    String defaultNote;

    @RequestMapping(value = "note/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getNote(HttpServletResponse response, @PathVariable("id") String id) {
        try {
            if(id == null) {
                id = defaultNote;
            }
            Note note = noteDB.getNote(id);
            HashMap<String, Object> retval = Maps.newHashMap();
            processMarkdown(note, retval);
            return retval;

        } catch (Exception e) {
            LOGGER.error("error loading note {}", id, e);
            response.setStatus(500);
            return null;
        }

    }

    private void processMarkdown(Note note, HashMap<String, Object> retval) {
        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(note.markdown);
        String html = renderer.render(document);
        html = String.format("<h1>%s</h1>\n", note.name) + html;
        retval.put("html", html);
    }

}
