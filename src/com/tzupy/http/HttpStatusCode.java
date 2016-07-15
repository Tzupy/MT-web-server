package com.tzupy.http;

/**
 * This class defines known http status codes.
 */
public abstract class HttpStatusCode {
    public final static String ok = "200 OK";
    public final static String badRequest = "400 Bad Request";
    public final static String notFound = "404 Not Found";
    public final static String internalError = "500 Internal Server Error";
    public final static String notImplemented = "501 Not Implemented";
}
