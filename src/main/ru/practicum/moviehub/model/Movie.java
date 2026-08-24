package ru.practicum.moviehub.model;

public class Movie implements Comparable<Movie> {
    private final String title;
    private final int year;

    public Movie(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int compareTo(Movie m) {
        return Integer.compare(this.year, m.year);
    }
}