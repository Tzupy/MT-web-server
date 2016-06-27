package com.tzupy.webserver;

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
     * Generates an html document with title and content.
     * @return the document's content formatted as a proper html
     */
    public String getHtml() {
        StringBuilder sb = new StringBuilder();

        sb.append(HtmlTag.doctype.toStartTag());
        sb.append(HtmlTag.html.toStartTag());
        sb.append(HtmlTag.head.toStartTag());
        sb.append(HtmlTag.title.toStartTag());
        sb.append(this.title);
        sb.append(HtmlTag.title.toEndTag());
        sb.append(HtmlTag.head.toEndTag());
        sb.append(HtmlTag.body.toStartTag());
        sb.append(this.content.toString());
        sb.append(HtmlTag.body.toEndTag());
        sb.append(HtmlTag.html.toEndTag());
        sb.append("\r\n");

        return sb.toString();
    }
}
