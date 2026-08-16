package ru.practicum.moviehub.api;

import java.util.List;

public class ErrorResponse {
    String errorMessage;
    List<String> details;

    public ErrorResponse(String message, List<String> details) {
        this.errorMessage = message;
    }
}