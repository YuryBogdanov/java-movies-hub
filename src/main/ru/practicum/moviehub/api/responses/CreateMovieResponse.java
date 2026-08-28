package ru.practicum.moviehub.api.responses;

public class CreateMovieResponse {
    private int id;

    public CreateMovieResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
