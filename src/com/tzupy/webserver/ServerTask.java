package com.tzupy.webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates a task to be run by the web server.
 */
public class ServerTask implements Callable<Void> {

    private static final Logger logger = Logger.getLogger("ServerTask");

    private final Socket client;

    /**
     * Class constructor receiving the client socket.
     * @param client The client socket.
     */
    public ServerTask(Socket client) {
        this.client = client;
    }

    /**
     * This writes from feedback on the socket output stream.
     * @return null
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {
        try {
            // write some feedback to the client
            Writer out = new OutputStreamWriter(client.getOutputStream());
            out.write("Happily connected!\r\n");
            out.write("Client IP is: " + client.getInetAddress().getHostAddress() + ":" + client.getPort() + "\r\n");
            out.flush();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO Exception: " + ex.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                    logger.log(Level.INFO, "Client disconnected");
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Couldn't close client socket: " + ex.getMessage());
                }
            }
        }

        return null;
    }
}
