package com.tzupy.html;

import com.tzupy.utils.ResourcePath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class returns requested content as html.
 */
public class HtmlContent {

    private static final Logger logger = Logger.getLogger(HtmlContent.class.getCanonicalName());

    private final StringBuilder buffer = new StringBuilder();

    /**
     * Gets a heading as html content.
     * @param line the line to be added as heading
     * @return the resulting html content
     */
    public String asHeading(String line) {
        buffer.setLength(0);
        buffer.append(HtmlTag.heading.toStartTag());
        buffer.append(line);
        buffer.append(HtmlTag.heading.toEndTag() + "\r\n");
        return buffer.toString();
    }

    /**
     * Gets a line break as html content.
     * @return the resulting html content
     */
    public String asLineBreak() {
        buffer.setLength(0);
        buffer.append(HtmlTag.lineBreak.toSelfClosingTag() + "\r\n");
        return buffer.toString();
    }

    /**
     * Gets an image as html content.
     * @param path the image's path
     * @param width the image's width
     * @param height the image's height
     * @param hspace the image's horizontal space
     * @return the resulting html content
     */
    public String asImage(String path, int width, int height, int hspace) {
        buffer.setLength(0);
        List<AttrPair> attributes = Arrays.asList(
                new AttrPair("src", "\"" + path + "\""),
                new AttrPair("width", "\"" + width + "\""),
                new AttrPair("height", "\"" + height + "\""),
                new AttrPair("hspace", "\"" + hspace + "\""));
        buffer.append(HtmlTag.image.toStartTag(attributes));
        buffer.append(HtmlTag.anchor.toEndTag());
        return buffer.toString();
    }

    /**
     * Gets an anchor link with text as html content.
     * @param path the anchor's path
     * @param text the anchor's text to display
     * @return the resulting html content
     */
    public String asAnchor(String path, String text) {
        buffer.setLength(0);
        buffer.append(HtmlTag.anchor.toStartTag(Arrays.asList(new AttrPair("href", "\"" + path + "\""))));
        buffer.append(text);
        buffer.append(HtmlTag.anchor.toEndTag());
        return buffer.toString();
    }

    /**
     * Gets an html table as html content.
     * @param table the table to be added
     * @return the resulting html content
     */
    public String asTable(Table table) {
        buffer.setLength(0);
        buffer.append(HtmlTag.table.toStartTag() + "\r\n");

        // append header
        buffer.append(HtmlTag.tableRow.toStartTag());
        for (String entry : table.getHeader()) {
            buffer.append(HtmlTag.tableHead.toStartTag());
            buffer.append(entry);
            buffer.append(HtmlTag.tableHead.toEndTag());
        }
        buffer.append(HtmlTag.tableRow.toEndTag() + "\r\n");

        // append rows
        for (String[] row : table.getRows()) {
            buffer.append(HtmlTag.tableRow.toStartTag());
            for (String entry : row) {
                buffer.append(HtmlTag.tableData.toStartTag());
                buffer.append(entry);
                buffer.append(HtmlTag.tableData.toEndTag());
            }
            buffer.append(HtmlTag.tableRow.toEndTag() + "\r\n");
        }

        buffer.append(HtmlTag.table.toEndTag() + "\r\n");
        return buffer.toString();
    }

    /**
     * Gets a stylesheet as html content.
     * @return the resulting html content
     */
    public String asStyle() {
        buffer.setLength(0);
        URL url;
        try {
            url = new URL(ResourcePath.style);

            buffer.append(HtmlTag.style.toStartTag() + "\r\n");

            // read the file line by line and append it to the content
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line + "\r\n");
                }
            } catch (IOException ex) {
                logger.severe("IO Exception: " + ex.getMessage());
            }

            buffer.append(HtmlTag.style.toEndTag() + "\r\n");
        } catch (MalformedURLException ex) {
            logger.severe("The resource URL is malformed: " + ex.getMessage());
        }

        return buffer.toString();
    }
}
