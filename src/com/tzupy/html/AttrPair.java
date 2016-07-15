package com.tzupy.html;

/**
 * This class defines an attribute pair for an html tag.
 */
public class AttrPair {
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
