package com.tzupy.webserver;

import com.tzupy.html.HtmlContent;
import com.tzupy.html.HtmlGenerator;
import com.tzupy.html.Table;
import com.tzupy.http.HttpRequest;
import com.tzupy.http.HttpResponse;
import com.tzupy.http.HttpStatusCode;
import com.tzupy.utils.FileUtils;
import com.tzupy.utils.ResourcePath;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * This class creates a task to be run by the web server.
 */
public class ServerTask implements Callable<Void> {

    private static final Logger logger = Logger.getLogger(ServerTask.class.getCanonicalName());

    private final Socket clientSocket;

    private final File root;

    private final HtmlContent htmlContent = new HtmlContent();
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
                htmlGenerator.addContent(htmlContent.asHeading(HttpStatusCode.notFound));
                String html = htmlGenerator.generateHtml();
                out.write(httpResponse.getMimeHeader(HttpStatusCode.notFound, html.length(), "text/html"));
                out.write(html);
                out.flush();
            } else if (!httpRequest.isMethodValid()) {
                htmlGenerator.addContent(htmlContent.asHeading(HttpStatusCode.notImplemented));
                String html = htmlGenerator.generateHtml();
                out.write(httpResponse.getMimeHeader(HttpStatusCode.notImplemented, html.length(), "text/html"));
                out.write(html);
                out.flush();
            } else if (!httpRequest.isProtocolValid()) {
                htmlGenerator.addContent(htmlContent.asHeading(HttpStatusCode.badRequest));
                String html = htmlGenerator.generateHtml();
                out.write(httpResponse.getMimeHeader(HttpStatusCode.badRequest, html.length(), "text/html"));
                out.write(html);
                out.flush();
            } else { // valid request
                if (url.isDirectory()) {
                    if (!url.canRead()) {
                        htmlGenerator.addLine("Client made no request");
                        htmlGenerator.addContent(htmlContent.asLineBreak());
                    } else {
                        //String ip = clientSocket.getInetAddress().getHostAddress();
                        //htmlGenerator.addLine("Client address is: " + ip + ":" + clientSocket.getPort());

                        htmlGenerator.addContent(htmlContent.asHeading("Index of " + httpRequest.getFilename()));

                        Table table = new Table();
                        table.setHeader(new String[] { "Name", "Last Modified", "Size" });

                        // add back navigation
                        if (!url.getCanonicalPath().equals(root.getCanonicalPath())) {
                            String name = htmlContent.asAnchor(".." + File.separator, "UP");
                            table.addRow(new String[] { name, "", "" } );
                        }

                        // list directories in alphabetical order
                        File[] directories = url.listFiles(File::isDirectory);
                        Arrays.sort(directories);
                        for (File dir : directories) {
                            String icon = htmlContent.asImage(ResourcePath.directory, 20, 20, 5);
                            String anchor = htmlContent.asAnchor(dir.getName() + File.separator, dir.getName());
                            String lastModified = FileUtils.getFileLastModifiedFormatted(new Date(dir.lastModified()));
                            table.addRow(new String[] { icon + anchor, lastModified, "-" } );
                        }

                        // list files in alphabetical order
                        File[] files = url.listFiles(File::isFile);
                        Arrays.sort(files);
                        for (File file : files) {
                            String icon = htmlContent.asImage(ResourcePath.file, 20, 20, 5);
                            String anchor = htmlContent.asAnchor(file.getName(), file.getName());
                            String lastModified = FileUtils.getFileLastModifiedFormatted(new Date(file.lastModified()));
                            String size = FileUtils.getFileSizeFormatted(file.length());
                            table.addRow(new String[] { icon + anchor, lastModified, size } );
                        }

                        htmlGenerator.addContent(htmlContent.asTable(table));

                        String html = htmlGenerator.generateHtml();
                        out.write(httpResponse.getMimeHeader(HttpStatusCode.ok, html.length(), "text/html"));
                        out.write(html);
                        out.flush();
                    }
                } else { // provides a file to the client
                    String contentType = new MimetypesFileTypeMap().getContentType(httpRequest.getUrl());
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
