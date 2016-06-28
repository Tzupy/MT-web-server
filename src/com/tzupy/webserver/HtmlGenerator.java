package com.tzupy.webserver;

import java.util.Arrays;

/**
 * This class generates the content of an HTML document.
 */
public class HtmlGenerator {

    private final String title;

    private final StringBuilder content = new StringBuilder();

    /**
     * Class constructor that receives a document title.
     * @param title the html document's title
     */
    public HtmlGenerator(String title) {
        this.title = title;
    }

    /**
     * Appends a div with a line to the document's content.
     * @param line the line to be added to the document
     */
    public void addLine(String line) {
        content.append(HtmlTag.paragraph.toStartTag());
        content.append(line);
        content.append(HtmlTag.paragraph.toEndTag());
    }

    /**
     * Appends a header with a line to the document's content.
     * @param line the line to be added to the document
     */
    public void addHeader(String line) {
        content.append(HtmlTag.header.toStartTag());
        content.append(line);
        content.append(HtmlTag.header.toEndTag());
    }

    /**
     * Appends a line break to the document's content.
     */
    public void addLineBreak() {
        content.append(HtmlTag.lineBreak.toStartTag() + "\r\n");
    }

    /**
     * Appends an anchor link to the document's content.
     */
    public void addAnchor(String ref) {
        content.append(HtmlTag.paragraph.toStartTag());
        content.append(HtmlTag.anchor.toStartTag(Arrays.asList(new AttrPair("href", "\"" + ref + "/\""))));
        content.append(ref);
        content.append(HtmlTag.anchor.toEndTag());
        content.append(HtmlTag.paragraph.toEndTag());
    }

    /**
     * Generates an html document with title and content.
     * @return the document's content formatted as a proper html
     */
    public String getHtml() {
        StringBuilder sb = new StringBuilder();

        sb.append(HtmlTag.doctype.toStartTag() + "\r\n");
        sb.append(HtmlTag.html.toStartTag() + "\r\n");
        sb.append(HtmlTag.head.toStartTag() + "\r\n");
        sb.append(HtmlTag.title.toStartTag() + "\r\n");
        sb.append(this.title + "\r\n");
        sb.append(HtmlTag.title.toEndTag() + "\r\n");
        sb.append(HtmlTag.head.toEndTag() + "\r\n");
        sb.append(HtmlTag.body.toStartTag() + "\r\n");
        sb.append(this.content.toString() + "\r\n");
        sb.append(HtmlTag.body.toEndTag() + "\r\n");
        sb.append(HtmlTag.html.toEndTag() + "\r\n");

        return sb.toString();
    }
}
