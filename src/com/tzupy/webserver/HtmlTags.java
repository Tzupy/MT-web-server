package com.tzupy.webserver;

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
     * Converts the current tag to an end tag.
     * @return the ending html tag
     */
    public String toEndTag() {
        return "</" + this.name + ">";
    }
}
