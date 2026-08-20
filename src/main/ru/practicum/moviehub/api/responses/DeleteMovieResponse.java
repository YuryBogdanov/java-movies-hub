package ru.practicum.moviehub.api.responses;

public class DeleteMovieResponse {
    private String result;

    public DeleteMovieResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
