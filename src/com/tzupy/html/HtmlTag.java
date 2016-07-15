package com.tzupy.html;

/**
 * This class defines several known html tags.
 */
public abstract class HtmlTag {

    public final static Tag docType = new Tag("!DOCTYPE html");
    public final static Tag html = new Tag("html");
    public final static Tag head = new Tag("head");
    public final static Tag link = new Tag("link");
    public final static Tag style = new Tag("style");
    public final static Tag title = new Tag("title");
    public final static Tag body = new Tag("body");
    public final static Tag paragraph = new Tag("div");
    public final static Tag lineBreak = new Tag("br");
    public final static Tag anchor = new Tag("a");
    public final static Tag heading = new Tag("h1");
    public final static Tag image = new Tag("img");
    public final static Tag table = new Tag("table");
    public final static Tag tableRow = new Tag("tr");
    public final static Tag tableHead = new Tag("th");
    public final static Tag tableData = new Tag("td");
}
