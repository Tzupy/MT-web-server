package com.tzupy.webserver;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;

/**
 * This class defines supported http request methods.
 */
abstract class RequestMethod {
    public final static String get = "GET";
    public final static String head = "HEAD";
    public final static String post = "POST";
    public final static String delete = "DELETE";
}

/**
 * This class parses an http request from the client.
 */
public class HttpRequest {

    private static final Logger logger = Logger.getLogger(ServerTask.class.getCanonicalName());

    private final int bufferSize = 1024;
    private final StringBuilder buffer = new StringBuilder(bufferSize);

    private final Reader in;

    private String method;
    private String filename;
    private File url;
    private String protocol;

    private boolean methodValid;
    private boolean protocolValid;

    /**
     * Class constructor that receives a reader from the client.
     * @param in the client's reader
     */
    public HttpRequest(Reader in) {
        this.in = in;
    }

    /**
     * Reads the first line of the client's request in a buffer.
     */
    public void read() {
        buffer.setLength(0);
        int c;
        try {
            while ((c = in.read()) != -1) {
                if (c == '\r' || c == '\n') break;
                buffer.append((char) c);
            }
        } catch (IOException ex) {
            logger.severe("IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Parses the client's request.
     * @throws IllegalArgumentException the request was malformed
     */
    public void parse(File root) throws IllegalArgumentException {
        String[] tokens = buffer.toString().split(" ");
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Malformed request.");
        }
        method = tokens[0];
        try {
            filename = URLDecoder.decode(tokens[1], "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.warning("Decoding Exception: " + ex.getMessage());
        }
        url = new File(root, filename);
        protocol = tokens[2];

        // check method validity
        if (!method.equals(RequestMethod.get)) {
            methodValid = false;
            throw new IllegalArgumentException("Unsupported method.");
        } else {
            methodValid = true;
        }

        // check protocol validity
        if (!protocol.toUpperCase().startsWith("HTTP/")) {
            protocolValid = false;
            throw new IllegalArgumentException("Unsupported protocol.");
        } else {
            protocolValid = true;
        }
    }

    /**
     * Gets the requested filename from the client.
     * @return the requested filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns true if the method is valid.
     * @return the method's validity
     */
    public boolean isMethodValid() {
        return methodValid;
    }

    /**
     * Returns true if the protocol is valid.
     * @return the protocol's validity
     */
    public boolean isProtocolValid() {
        return protocolValid;
    }

    /**
     * Gets the requested url.
     * @return the url
     */
    public File getUrl() {
        return url;
    }
}

