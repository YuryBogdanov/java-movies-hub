package ru.practicum.moviehub.http.handlers.movies;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.api.CreateMovieRequest;
import ru.practicum.moviehub.api.responses.CreateMovieResponse;
import ru.practicum.moviehub.http.handlers.HttpResponder;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CreateMovieAction implements MovieAction {
    private final int minYear = 1888;

    private final MoviesStore store;
    private final HttpResponder responder;

    public CreateMovieAction(MoviesStore store, HttpResponder responder) {
        this.store = store;
        this.responder = responder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            CreateMovieRequest request = parseRequestBody(exchange);
            CreateMovieRequestValidationResult validationResult = validateRequest(request);
            if (validationResult.getFullValidationMessage().isEmpty()) {
                Movie movie = new Movie(
                        request.getTitle(),
                        request.getYear()
                );
                String id = store.storeMovie(movie);
                responder.sendSuccess(exchange, 201, new CreateMovieResponse(id));
            } else {
                responder.sendError(exchange, 422, validationResult.getFullValidationMessage());
            }
        } catch (Exception e) {
            responder.sendError(exchange, 500, "Something went wrong");
        }
    }

    private CreateMovieRequest parseRequestBody(HttpExchange exchange) throws JsonSyntaxException, IOException {
        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, CreateMovieRequest.class);
        }
    }

    private CreateMovieRequestValidationResult validateRequest(CreateMovieRequest request) {
        return new CreateMovieRequestValidationResult(
                validateTitle(request.getTitle()) ? null : "The title should be up to 100 characters",
                validateYear(request.getYear()) ? null : "The year should be between 1888 and current year"
        );
    }

    private boolean validateTitle(String title) {
        if (title == null || title.isBlank()) {
            return false;
        }
        return title.length() <= 100;
    }

    private boolean validateYear(int year) {
        return minYear <= year && year <= java.time.LocalDate.now().getYear() + 1;
    }
}

class CreateMovieRequestValidationResult {
    private final String titleValidationResult;
    private final String yearValidationResult;

    CreateMovieRequestValidationResult(String titleValidationResult, String yearValidationResult) {
        this.titleValidationResult = titleValidationResult;
        this.yearValidationResult = yearValidationResult;
    }

    String getTitleValidationResult() {
        return titleValidationResult;
    }

    String getYearValidationResult() {
        return yearValidationResult;
    }

    String getFullValidationMessage() {
        StringBuilder errorMessageBuilder = new StringBuilder();
        if (getTitleValidationResult() != null) {
            errorMessageBuilder.append(getTitleValidationResult());
            errorMessageBuilder.append("; ");
        }
        if (getYearValidationResult() != null) {
            errorMessageBuilder.append(getYearValidationResult());
        }

        return errorMessageBuilder.toString();
    }
}