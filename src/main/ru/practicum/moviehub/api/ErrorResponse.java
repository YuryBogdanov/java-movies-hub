package ru.practicum.moviehub.api;

import java.util.List;
import java.util.Optional;

public class ErrorResponse {
    private String error;
    private Optional<List<String>> details;

    public ErrorResponse(String message, List<String> details) {
        this.error = message;
        this.details = Optional.of(details);
    }

    public ErrorResponse(String message) {
        this.error = message;
        this.details = Optional.empty();
    }
}