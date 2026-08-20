package ru.practicum.moviehub.api;

import java.util.List;

public class ErrorResponse {
    String error;
    List<String> details;

    public ErrorResponse(String message, List<String> details) {
        this.error = message;
        this.details = details;
    }
}