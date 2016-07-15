package com.tzupy.html;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a table with header and rows.
 */
public class Table {

    private String[] header;
    private List<String[]> rows = new ArrayList<>();

    /**
     * Sets the table's header.
     * @param header the table's header
     */
    public void setHeader(String[] header) {
        this.header = header;
    }

    /**
     * Adds a new row to the table.
     * @param row the row to be added
     */
    public void addRow(String[] row) {
        rows.add(row);
    }

    /**
     * Gets the table's header.
     * @return the table's header
     */
    public String[] getHeader() {
        return header;
    }

    /**
     * Gets the table's rows.
     * @return the table's rows
     */
    public List<String[]> getRows() {
        return rows;
    }
}
