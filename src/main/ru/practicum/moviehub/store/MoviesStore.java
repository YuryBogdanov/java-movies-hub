package ru.practicum.moviehub.store;

import ru.practicum.moviehub.model.Movie;

import java.util.HashMap;

public class MoviesStore {
    private HashMap<String, Movie> storage = new HashMap<>();

    public String storeMovie(Movie movie) {
        String id = java.util.UUID.randomUUID().toString();

        storage.put(id, movie);

        return id;
    }
}