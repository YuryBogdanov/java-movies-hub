package ru.practicum.moviehub.api.requests;

public class CreateMovieRequest {
    private String title;
    private int year;

    public CreateMovieRequest(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }
}
