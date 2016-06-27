package com.tzupy.webserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * This class creates a task to be run by the web server.
 */
public class ServerTask implements Callable<Void> {

    private static final Logger logger = Logger.getLogger(ServerTask.class.getCanonicalName());

    private final Socket clientSocket;

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
            // write some feedback to the client
            Writer out = new OutputStreamWriter(clientSocket.getOutputStream());
            out.write("Happily connected!\r\n");
            String ip = clientSocket.getInetAddress().getHostAddress();
            out.write("Client address is: " + ip + ":" + clientSocket.getPort() + "\r\n");
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
