package ru.practicum.moviehub.api.requests;

public class GetMovieRequest {
    private final String id;

    public GetMovieRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
