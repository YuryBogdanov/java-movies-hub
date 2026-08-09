package ru.practicum.moviehub.api.responses;

public class CreateMovieResponse {
    private String id;

    public CreateMovieResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
