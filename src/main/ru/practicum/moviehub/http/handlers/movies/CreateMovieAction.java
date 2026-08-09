package ru.practicum.moviehub.http.handlers.movies;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.api.CreateMovieRequest;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class CreateMovieAction implements MovieAction {
    private final int minYear = 1888;

    private MoviesStore store;

    public CreateMovieAction(MoviesStore store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        try {
            CreateMovieRequest request = parseRequestBody(exchange);
            if (validateRequest(request)) {

            } else {
                // TODO: отправлять осознанную ошибку
                exchange.sendResponseHeaders(422, -1);
            }
        } catch (JsonSyntaxException | IOException e) {
            exchange.sendResponseHeaders(400, -1);
        }
    }

    private CreateMovieRequest parseRequestBody(HttpExchange exchange) throws JsonSyntaxException, IOException {
        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, CreateMovieRequest.class);
        }
    }

    private boolean validateRequest(CreateMovieRequest request) {
        return validateTitle(request.getTitle()) && validateYear(request.getYear());
    }

    private boolean validateTitle(String title) {
        if (title.isBlank()) {
            return false;
        }
        return title.length() <= 100;
    }

    private boolean validateYear(int year) {
        return minYear <= year && year <= java.time.LocalDate.now().getYear() + 1;
    }
}
