package com.tzupy.webserver;

import java.util.List;

/**
 * This class defines several known html tags.
 */
abstract class HtmlTag {

    public final static Tag doctype = new Tag("!doctype html");
    public final static Tag html = new Tag("html");
    public final static Tag head = new Tag("head");
    public final static Tag title = new Tag("title");
    public final static Tag body = new Tag("body");
    public final static Tag paragraph = new Tag("div");
    public final static Tag lineBreak = new Tag("br");
    public final static Tag anchor = new Tag("a");
    public final static Tag header = new Tag("h1");
    public final static Tag image = new Tag("img");
}

/**
 * This class defines an attribute pair for an html tag.
 */
class AttrPair {
    public final String attr;
    public final String value;

    /**
     * Class constructor that receives an attribute name and a value.
     * @param attr attribute name
     * @param value attribute value
     */
    public AttrPair(String attr, String value) {
        this.attr = attr;
        this.value = value;
    }
}

/**
 * This class converts tags to html tags.
 */
class Tag {

    private String name;

    /**
     * Class constructor that receives a tag name.
     * @param name The tag's name.
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Converts the current tag to a start tag.
     * @return the starting html tag
     */
    public String toStartTag() {
        return "<" + this.name + ">";
    }

    /**
     * Converts the current tag to a start tag and adds attribute.
     * @param attributes list of attributes for a tag
     * @return the starting html tag with all the attributes
     */
    public String toStartTag(List<AttrPair> attributes) {
        StringBuilder sb = new StringBuilder("<" + this.name);
        for (AttrPair pair : attributes) {
            sb.append(" " + pair.attr + "=" + pair.value);
        }
        sb.append(">");
        return sb.toString();
    }

    /**
     * Converts the current tag to an end tag.
     * @return the ending html tag
     */
    public String toEndTag() {
        return "</" + this.name + ">";
    }

    /**
     * Gets the name of the current tag.
     * @return the tag's name
     */
    @Override
    public String toString() {
        return this.name;
    }
}
