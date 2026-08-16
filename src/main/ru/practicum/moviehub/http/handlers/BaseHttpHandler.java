package ru.practicum.moviehub.http.handlers;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public abstract class BaseHttpHandler implements HttpHandler {
    private final String CT_JSON = "application/json; charset=UTF-8";
    protected HttpResponder responder = new HttpResponder();

    protected boolean validateContentType(HttpExchange exchange) {
        String ctHeader = exchange.getRequestHeaders().get("Content-Type").getFirst();

        if (ctHeader == null || !ctHeader.equals(CT_JSON)) {
            try {
                responder.sendError(
                        exchange,
                        415,
                        "Unsupported content type",
                        List.of("Expected 'application/json; charset=UTF-8'")
                );
            } catch (IOException e) {
                System.out.println("=== O kurwa");
            }
            return false;
        }
        return true;
    }
}