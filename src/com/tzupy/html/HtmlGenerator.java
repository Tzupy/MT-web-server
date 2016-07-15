package com.tzupy.html;

import com.tzupy.utils.ResourcePath;

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
        content.append(HtmlTag.paragraph.toEndTag() + "\r\n");
    }

    /**
     * Appends valid html content to the document's content.
     * @param htmlContent the text to be added to the document's content
     */
    public void addContent(String htmlContent) {
        content.append(htmlContent);
    }

    /**
     * Generates an html document with title and content.
     * @return the document's content formatted as a proper html
     */
    public String generateHtml() {
        StringBuilder sb = new StringBuilder();

        sb.append(HtmlTag.docType.toStartTag() + "\r\n");
        sb.append(HtmlTag.html.toStartTag() + "\r\n");
        sb.append(HtmlTag.head.toStartTag() + "\r\n");
        sb.append(HtmlTag.title.toStartTag());
        sb.append(this.title);
        sb.append(HtmlTag.title.toEndTag() + "\r\n");
        sb.append(HtmlTag.link.toSelfClosingTag(Arrays.asList(new AttrPair("rel", "\"shortcut icon\""),
                new AttrPair("type", "\"image/png\""),
                new AttrPair("href", "\"" + ResourcePath.favIcon + "\""))) + "\r\n");
        /*sb.append(HtmlTag.link.toSelfClosingTag(Arrays.asList(new AttrPair("rel", "\"stylesheet\""),
                new AttrPair("type", "\"text/css\""),
                new AttrPair("href", "\"" + ResourcePath.style + "\""))) + "\r\n");*/
        sb.append(new HtmlContent().asStyle());
        sb.append(HtmlTag.head.toEndTag() + "\r\n");
        sb.append(HtmlTag.body.toStartTag() + "\r\n");
        sb.append(this.content.toString());
        sb.append(HtmlTag.body.toEndTag() + "\r\n");
        sb.append(HtmlTag.html.toEndTag() + "\r\n");

        return sb.toString();
    }
}
