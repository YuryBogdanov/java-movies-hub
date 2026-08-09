package ru.practicum.moviehub.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponder {
    private final Gson gson = new Gson();
    private final String CT_JSON = "application/json; charset=UTF-8";

    public void sendJson(HttpExchange ex, int status, String json) throws IOException {
        ex.getResponseHeaders().set("Content-Type", CT_JSON);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
