package ru.practicum.moviehub.http.handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.api.ErrorResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpResponder {
    private final String CT_JSON = "application/json; charset=UTF-8";
    private final Gson gson = new Gson();

    public void sendSuccess(HttpExchange exchange, int status, Object object) throws IOException {
        writeResponse(exchange, status, object);
    }

    public void sendError(HttpExchange exchange, int status, String errorMessage, List<String> details) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, details);
        writeResponse(exchange, status, errorResponse);
    }

    private void writeResponse(HttpExchange exchange, int status, Object wrappedObject) throws IOException {
        byte[] bytes = gson.toJson(wrappedObject).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", CT_JSON);
        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
