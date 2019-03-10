package com.devsmart.thoughplot;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.File;
import java.io.IOException;

public class ViewEngine {

    public void addNoteFromFile(File file) throws IOException {
        String str = Files.asByteSource(file).asCharSource(Charsets.UTF_8).read();
        addNoteFromMarkdown(str);
    }

    public void addNoteFromMarkdown(String str) {

        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(str);
        String html = renderer.render(document);
        System.out.println(html);

    }
}
