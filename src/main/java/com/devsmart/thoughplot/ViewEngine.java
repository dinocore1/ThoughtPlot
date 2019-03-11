package com.devsmart.thoughplot;

import com.google.common.collect.Sets;
import com.vladsch.flexmark.ext.wikilink.WikiLink;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;

//@Component
//@Scope(WebApplicationContext.SCOPE_SESSION)
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ViewEngine {

    private final Parser mMarkdownParser;
    private HtmlRenderer mHtmlRenderer;

    Graph<Note, DefaultEdge> mGraph = GraphTypeBuilder.<Note, DefaultEdge>undirected()
            .allowingSelfLoops(false)
            .allowingMultipleEdges(false)
            .weighted(false)
            .edgeClass(DefaultEdge.class)
            .buildGraph();

    private TreeMap<String, Note> mNoteIndex = new TreeMap<String, Note>();

    private NodeVisitor createLinkRefVisitor(Note rootNote) {
        return new NodeVisitor(new VisitHandler<WikiLink>(WikiLink.class, new Visitor<WikiLink>(){
            @Override
            public void visit(WikiLink node) {
                String linkRefName = node.getPageRef().toLowerCase().toString();
                Note n = mNoteIndex.get(linkRefName);
                if(n == null) {
                    n = new Note(linkRefName);
                    mGraph.addVertex(n);
                    mNoteIndex.put(linkRefName, n);
                }
                mGraph.addEdge(rootNote, n);
            }
        }));
    }

    private static class WikiLinkRenderExt implements HtmlRenderer.HtmlRendererExtension {

        @Override
        public void rendererOptions(MutableDataHolder options) {

        }

        @Override
        public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
            rendererBuilder.attributeProviderFactory(WikiLinkAttributeProvider.Factory());
        }

        public static WikiLinkRenderExt create() {
            return new WikiLinkRenderExt();
        }
    }

    private static class WikiLinkAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(final Node node, final AttributablePart part, final Attributes attributes) {
            if (node instanceof WikiLink && part == AttributablePart.LINK) {
                WikiLink wikiNode = (WikiLink) node;
                attributes.replaceValue("href", "?" + wikiNode.getLink().toLowerCase().toString());
                //attributes.replaceValue("class", "my-autolink-class");

            }
        }

        static AttributeProviderFactory Factory() {
            return new IndependentAttributeProviderFactory() {
                @Override
                public AttributeProvider create(LinkResolverContext context) {
                    return new WikiLinkAttributeProvider();
                }
            };
        }
    }

    public ViewEngine() {
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, Arrays.asList(WikiLinkExtension.create(), WikiLinkRenderExt.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        mMarkdownParser = Parser.builder(options).build();
        mHtmlRenderer = HtmlRenderer.builder(options).build();
    }


    public Note loadMarkdown(String name, String markdown) {
        Note n = mNoteIndex.get(name);
        if(n == null) {
            n = new Note(name);
            mNoteIndex.put(name, n);
            mGraph.addVertex(n);
        }

        n.markdown = markdown;
        Document document = mMarkdownParser.parse(markdown);
        createLinkRefVisitor(n).visit(document);
        n.html = mHtmlRenderer.render(document);
        return n;
    }

    public Graph<Note, DefaultEdge> getNeighbors(String name, int max) {
        Note n = mNoteIndex.get(name);
        if(n != null) {
            BreadthFirstIterator<Note, DefaultEdge> bfs = new BreadthFirstIterator<>(mGraph, n);
            Set<Note> verticies = Sets.newHashSet();
            while(bfs.hasNext() && verticies.size() < max) {
                verticies.add(bfs.next());
            }

            return new AsSubgraph<>(mGraph, verticies);
        }

        return null;
    }

}
