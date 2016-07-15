package com.tzupy.html;

import java.util.List;

/**
 * This class converts tags to html tags.
 */
public class Tag {

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
     * Converts the current tag to a start tag and adds attributes.
     * @param attributes list of attributes for a tag
     * @return the starting html tag with all the attributes
     */
    public String toStartTag(List<AttrPair> attributes) {
        StringBuilder sb = new StringBuilder("<" + this.name);
        for (AttrPair pair : attributes) {
            sb.append(" ").append(pair.attr).append("=").append(pair.value);
        }
        sb.append(">");
        return sb.toString();
    }

    /**
     * Converts the current tag to a self-closing tag.
     * @return the self-closing html tag
     */
    public String toSelfClosingTag() {
        return "<" + this.name + " />";
    }

    /**
     * Converts the current tag to a self-closing tag and adds attributes.
     * @param attributes list of attributes for a tag
     * @return the self-closing html tag with all the attributes
     */
    public String toSelfClosingTag(List<AttrPair> attributes) {
        StringBuilder sb = new StringBuilder("<" + this.name);
        for (AttrPair pair : attributes) {
            sb.append(" ").append(pair.attr).append("=").append(pair.value);
        }
        sb.append(" />");
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
