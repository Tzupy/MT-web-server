package com.tzupy.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates a multi-threaded web server with thread-pooling.
 */
public class WebServer {

    private static final Logger logger = Logger.getLogger("WebServer");

    private static final int DEFAULT_PORT = 8080;
    private static final int MAX_THREADS = 50;

    /**
     * Main program entry.
     * @param args (optional) First argument must be a port number in range (0-65535).
     */
    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        // read the port as a program argument if provided, otherwise use the default one
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                logger.log(Level.SEVERE, "Port not a number: " + ex.getMessage());
            }
        }

        try (ServerSocket server = new ServerSocket(port)) {
            String ip = server.getInetAddress().getLocalHost().getHostAddress();
            logger.log(Level.INFO, "Server started at address: " + ip + ":" + server.getLocalPort());

            // listen for client connections
            while (true) {
                Socket client;
                try {
                    // accept client connection
                    client = server.accept();
                    logger.log(Level.INFO, "Client connected");

                    // start a server thread
                    ServerTask serverTask = new ServerTask(client);
                    threadPool.submit(serverTask);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Client couldn't connect: " + ex.getMessage());
                } catch (RejectedExecutionException ex) {
                    logger.log(Level.SEVERE, "Couldn't start server task: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Couldn't start web server at port " + port + ": " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.log(Level.SEVERE, "Port " + port + " outside of range (0 - 65535): " + ex.getMessage());
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, "Runtime exception: ", ex);
        }

        threadPool.shutdown();
    }
}
