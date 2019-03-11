package com.devsmart.thoughplot;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Maps;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1")
public class RESTApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTApi.class);

    @Autowired
    NoteDB noteDB;

    @Autowired
    ViewEngine viewEngine;

    public static class NoteData {
        public String id;
        public String markdown;
        public String html;
        public Map<String, Object> graph;
    }

    @RequestMapping(value = "note/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NoteData getNote(HttpServletResponse response, @PathVariable("id") String id) {
        try {
            Note note = noteDB.getNote(id);
            if(note == null) {
                response.setStatus(404);
                return null;
            }

            note = viewEngine.loadMarkdown(id, note.markdown);
            NoteData retval = new NoteData();
            retval.id = id;
            retval.markdown = note.markdown;
            retval.html = String.format("<h1>%s</h1>\n<a href=\"#\" class=\"tp-edit\">Edit</a>", StringUtils.capitalize(note.name)) + note.html;

            addGraph(retval, viewEngine.getNeighbors(id, 17));

            return retval;

        } catch (Exception e) {
            LOGGER.error("error loading note {}", id, e);
            response.setStatus(500);
            return null;
        }

    }


    private static class JsonGraphNode {
        public String id;
        public String label;

        public JsonGraphNode(String name) {
            this.id = name;
            this.label = name;
        }
    }

    public static class JsonGraphEdge {
        public String id;
        public String from;
        public String to;

        public JsonGraphEdge(String from, String to) {
            this.from = from;
            this.to = to;

            /* Let's sort the two node names alphabetically to ensure uniqueness */
            if(from.compareTo(to) < 0) {
                this.id = String.format("%s-%s", from, to);
            } else {
                this.id = String.format("%s-%s", to, from);
            }

        }
    }

    private static void addGraph(NoteData retval, Graph<Note, DefaultEdge> graph) {

        retval.graph = Maps.newHashMap();

        List<JsonGraphNode> nodes = new LinkedList<>();
        retval.graph.put("nodes", nodes);
        for(Note n : graph.vertexSet()) {
            nodes.add(new JsonGraphNode(n.name));
        }

        List<JsonGraphEdge> edges = new LinkedList<>();
        retval.graph.put("edges", edges);
        for(DefaultEdge e : graph.edgeSet()) {
            edges.add(new JsonGraphEdge( graph.getEdgeSource(e).name, graph.getEdgeTarget(e).name ));
        }

    }

}
