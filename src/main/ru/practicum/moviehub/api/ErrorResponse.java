package ru.practicum.moviehub.api;

public class ErrorResponse {
    String errorMessage;

    public ErrorResponse(String message) {
        this.errorMessage = message;
    }
}