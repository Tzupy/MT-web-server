package com.tzupy.webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * This class creates a task to be run by the web server.
 */
public class ServerTask implements Callable<Void> {

    private static final Logger logger = Logger.getLogger(ServerTask.class.getCanonicalName());

    private final Socket clientSocket;

    private final int bufferSize = 1024;
    private final StringBuilder buffer = new StringBuilder(bufferSize);

    /**
     * Class constructor receiving the client socket.
     * @param clientSocket The client socket.
     */
    public ServerTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * This writes some feedback on the socket output stream.
     * @return null
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {
        try {
            Reader in = new InputStreamReader(new BufferedInputStream(clientSocket.getInputStream()));
            Writer out = new OutputStreamWriter(new BufferedOutputStream(clientSocket.getOutputStream()));

            HtmlGenerator htmlGenerator = new HtmlGenerator("Web server");

            String ip = clientSocket.getInetAddress().getHostAddress();
            htmlGenerator.addLine("Client address is: " + ip + ":" + clientSocket.getPort());

            // read client request in a buffer
            buffer.setLength(0);
            int c;
            while ((c = in.read()) != -1) {
                if (c == '\r' || c == '\n' || c == -1) break;
                buffer.append((char) c);
            }

            htmlGenerator.addLine("Client request was: " + buffer.toString());

            out.write(htmlGenerator.getHtml());

            out.flush();
        } catch (IOException ex) {
            logger.severe("IO Exception: " + ex.getMessage());
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                    logger.info("Client disconnected");
                } catch (IOException ex) {
                    logger.severe("Couldn't close client socket: " + ex.getMessage());
                }
            }
        }

        return null;
    }
}
