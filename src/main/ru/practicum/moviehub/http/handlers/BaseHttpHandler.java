package ru.practicum.moviehub.http.handlers;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public abstract class BaseHttpHandler implements HttpHandler {
    private final String CT_JSON = "application/json; charset=UTF-8";
    protected HttpResponder responder = new HttpResponder();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> handleGetRequest(exchange);
            case "POST" -> handlePostRequest(exchange);
            case "DELETE" -> handleDeleteRequest(exchange);
            default -> responder.sendError(exchange, 405, "Method not allowed", List.of());
        }
    }

    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        responder.sendError(exchange, 405, "Method not allowed", List.of());
    }

    protected void handlePostRequest(HttpExchange exchange) throws IOException {
        responder.sendError(exchange, 405, "Method not allowed", List.of());
    }

    protected void handleDeleteRequest(HttpExchange exchange) throws IOException {
        responder.sendError(exchange, 405, "Method not allowed", List.of());
    }

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