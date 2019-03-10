package com.devsmart.thoughplot;

import com.vladsch.flexmark.ast.AutoLink;
import com.vladsch.flexmark.ast.LinkRef;
import com.vladsch.flexmark.ext.wikilink.WikiLink;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.util.Arrays;
import java.util.Collection;

public class ViewEngine {

    private final Parser mMarkdownParser;
    private HtmlRenderer mHtmlRenderer;

    private static NodeVisitor createLinkRefVisitor(Collection<String> linkRefs) {
        return new NodeVisitor(new VisitHandler<WikiLink>(WikiLink.class, new Visitor<WikiLink>(){
            @Override
            public void visit(WikiLink node) {
                String linkRefName = node.getPageRef().toLowerCase().toString();
                linkRefs.add(linkRefName);
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


    public void addNote(Note note) {
        processNote(note);
    }

    public void processNote(Note note) {
        Document document = mMarkdownParser.parse(note.markdown);
        createLinkRefVisitor(note.linkRefs).visit(document);
        note.html = mHtmlRenderer.render(document);
    }
}
