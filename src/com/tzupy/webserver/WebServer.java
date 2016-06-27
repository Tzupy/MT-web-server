package com.tzupy.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;

/**
 * This class creates a multi-threaded web server with thread-pooling.
 */
public class WebServer {

    private static final Logger logger = Logger.getLogger(WebServer.class.getCanonicalName());

    private static final int DEFAULT_PORT = 8080;
    private static final int MAX_THREADS = 50;

    private int port;

    private ServerSocket serverSocket;

    private ExecutorService threadPool;

    /**
     * Class constructor that uses a given port.
     */
    public WebServer(int port) {
        this.port = port;
    }

    /**
     * Starts the web server.
     */
    public void start() {
        threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        try {
            serverSocket = new ServerSocket(port);
            String ip = serverSocket.getInetAddress().getLocalHost().getHostAddress();
            logger.info("Server started at address: " + ip + ":" + serverSocket.getLocalPort());

            // listen for client connections
            while (true) {
                Socket client;
                try {
                    client = serverSocket.accept();
                    logger.info("Client connected");

                    ServerTask serverTask = new ServerTask(client);
                    threadPool.submit(serverTask);
                } catch (IOException ex) {
                    logger.severe("Client couldn't connect: " + ex.getMessage());
                } catch (RejectedExecutionException ex) {
                    logger.severe("Couldn't start server task: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            logger.severe("Couldn't start web server at port " + port + ": " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.severe("Port " + port + " outside of range (0 - 65535): " + ex.getMessage());
        } catch (RuntimeException ex) {
            logger.severe("Runtime exception: " + ex.getMessage());
        } finally {
            stop();
        }
    }

    /**
     * Closes the server and thread pool.
     */
    public void stop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                logger.info("Server stopped");
            } catch (IOException ex) {
                logger.severe("Couldn't stop web server: " + ex.getMessage());
            }
        }

        threadPool.shutdown();
    }

    /**
     * Main program entry.
     * @param args (optional) First argument must be a port number in range (0-65535).
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);

                // if port is out of range, use the default one
                if (port < 0 || port > 65535) {
                    logger.warning("Port not in range (0-65535), using default one");
                    port = DEFAULT_PORT;
                }
            } catch (NumberFormatException ex) {
                logger.severe("Port not a number: " + ex.getMessage());
            }
        }

        // create and start web server
        WebServer server = new WebServer(port);
        if (server != null) {
            server.start();
        }
    }
}
