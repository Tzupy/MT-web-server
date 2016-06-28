package com.tzupy.webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Logger;

/**
 This class creates a response for the client's request.
 */
public class HttpResponse {

    private static final Logger logger = Logger.getLogger(ServerTask.class.getCanonicalName());

    private final File root;

    /**
     * Class constructor that receives the server root.
     * @param root the root of the server
     */
    public HttpResponse(File root) {
        this.root = root;
    }

    /**
     * Creates the MIME header.
     * @param statusCode http status code
     * @param contentLength the file length
     * @param contentType the file type
     * @return the MIME header
     */
    public String getMimeHeader(String statusCode, long contentLength, String contentType) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 " + statusCode + "\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Server: Web server\r\n");
        sb.append("Last-Modified: " + new Date(root.lastModified()) + "\r\n");
        sb.append("Content-Length: " + contentLength + "\r\n");
        sb.append("Content-Type: " + contentType + "; charset=utf-8\r\n\r\n");
        return sb.toString();
    }

    /**
     * Reads the file's content.
     * @return the file's content
     */
    public byte[] getBody() {
        try {
            byte[] data = Files.readAllBytes(root.toPath());
            return data;
        } catch (IOException ex) {
            logger.severe("IO exception: " + ex.getMessage());
        }
        return null;
    }
}
