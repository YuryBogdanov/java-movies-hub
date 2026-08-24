package ru.practicum.moviehub.api;

import java.util.List;
import java.util.Optional;

public class ErrorResponse {
    private String error;
    private List<String> details;

    public ErrorResponse(String message, List<String> details) {
        this.error = message;
        this.details = details;
    }

    public ErrorResponse(String message) {
        this.error = message;
        this.details = List.of();
    }

    public String getError() {
        return error;
    }

    public List<String> getDetails() {
        return details;
    }
}