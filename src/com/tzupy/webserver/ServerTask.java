package com.tzupy.webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

abstract class HttpStatusCode {
    public final static String ok = "200 OK";
    public final static String badRequest = "400 Bad Request";
    public final static String notFound = "404 Not Found";
    public final static String internalError = "500 Internal Server Error";
    public final static String notImplemented = "501 Not Implemented";
}

/**
 * This class creates a task to be run by the web server.
 */
public class ServerTask implements Callable<Void> {

    private static final Logger logger = Logger.getLogger(ServerTask.class.getCanonicalName());

    private final Socket clientSocket;

    private final File root;

    private final HtmlGenerator htmlGenerator = new HtmlGenerator("Web server");
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    /**
     * Class constructor receiving the client socket and server root.
     * @param clientSocket the client socket
     * @param root the root of the server
     */
    public ServerTask(Socket clientSocket, File root) {
        this.clientSocket = clientSocket;
        this.root = root;
    }

    /**
     * Parses the http request from the client.
     * @param in the client's reader
     */
    public void handleRequest(Reader in) {
        httpRequest = new HttpRequest(in);
        httpRequest.read();

        try {
            httpRequest.parse(root);
            logger.info("Client requested url: " + httpRequest.getUrl());
        } catch (IllegalArgumentException ex) {
            logger.severe("Client's request is malformed: " + ex.getMessage());
        }
    }

    /**
     * Creates an http response for the client.
     * @param out the client's writer
     * @param fileWriter the writer for a file
     */
    public void handleResponse(Writer out, OutputStream fileWriter) {
        File url = httpRequest.getUrl();
        httpResponse = new HttpResponse(url);

        try {
            if (!url.exists()) {
                htmlGenerator.addHeader(HttpStatusCode.notFound);
                out.write(httpResponse.getMimeHeader(HttpStatusCode.notFound, htmlGenerator.getHtml().length(), "text/html"));
                out.write(htmlGenerator.getHtml());
                out.flush();
            } else if (!httpRequest.isMethodValid()) {
                htmlGenerator.addHeader(HttpStatusCode.notImplemented);
                out.write(httpResponse.getMimeHeader(HttpStatusCode.notImplemented, htmlGenerator.getHtml().length(), "text/html"));
                out.write(htmlGenerator.getHtml());
                out.flush();
            } else if (!httpRequest.isProtocolValid()) {
                htmlGenerator.addHeader(HttpStatusCode.badRequest);
                out.write(httpResponse.getMimeHeader(HttpStatusCode.badRequest, htmlGenerator.getHtml().length(), "text/html"));
                out.write(htmlGenerator.getHtml());
                out.flush();
            } else { // valid request
                if (url.isDirectory()) {
                    String ip = clientSocket.getInetAddress().getHostAddress();
                    htmlGenerator.addLine("Client address is: " + ip + ":" + clientSocket.getPort());
                    htmlGenerator.addLineBreak();

                    if (!url.canRead()) {
                        htmlGenerator.addLine("Client made no request");
                        htmlGenerator.addLineBreak();
                    } else {
                        htmlGenerator.addHeader("Index of " + url);
                        htmlGenerator.addLineBreak();

                        // add back navigation
                        if (!url.getCanonicalPath().equals(root.getCanonicalPath())) {
                            htmlGenerator.addAnchor(".." + File.separator, "Back");
                            htmlGenerator.addLineBreak();
                            htmlGenerator.addLineBreak();
                        }
                        // list directories in alphabetical order
                        File[] directories = url.listFiles(File::isDirectory);
                        Arrays.sort(directories);
                        for (File dir : directories) {
                            //htmlGenerator.addImage(ResourceType.directory);
                            htmlGenerator.addAnchor(dir.getName() + File.separator, dir.getName() + File.separator);
                            htmlGenerator.addLineBreak();
                        }

                        // list files in alphabetical order
                        File[] files = url.listFiles(File::isFile);
                        Arrays.sort(files);
                        for (File file : files) {
                            //htmlGenerator.addImage(ResourceType.file);
                            htmlGenerator.addAnchor(file.getName(), file.getName());
                            htmlGenerator.addLineBreak();
                        }

                        String html = htmlGenerator.getHtml();
                        out.write(httpResponse.getMimeHeader(HttpStatusCode.ok, html.length(), "text/html"));
                        out.write(html);
                        out.flush();
                    }
                } else { // provides a file to the client
                    String contentType = URLConnection.getFileNameMap().getContentTypeFor(httpRequest.getFilename());
                    try {
                        out.write(httpResponse.getMimeHeader(HttpStatusCode.ok, httpResponse.getBody().length, contentType));
                        out.flush();
                        fileWriter.write(httpResponse.getBody());
                        fileWriter.flush();
                    } catch (IOException ex) {
                        logger.severe("IO Exception: " + ex.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            logger.severe("IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Processes the client request and sends a response.
     * @return null
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {
        try {
            Reader in = new InputStreamReader(new BufferedInputStream(clientSocket.getInputStream()));
            OutputStream fileWriter = new BufferedOutputStream(clientSocket.getOutputStream());
            Writer out = new OutputStreamWriter(fileWriter);

            handleRequest(in);
            handleResponse(out, fileWriter);
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                    //logger.info("Client disconnected");
                } catch (IOException ex) {
                    logger.severe("Couldn't close client socket: " + ex.getMessage());
                }
            }
        }

        return null;
    }
}
