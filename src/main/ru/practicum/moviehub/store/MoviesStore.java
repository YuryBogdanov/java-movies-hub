package ru.practicum.moviehub.store;

import ru.practicum.moviehub.model.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MoviesStore {
    private HashMap<String, Movie> storage = new HashMap<>();

    public String storeMovie(Movie movie) {
        Optional<String> possibleId = checkIfMovieExists(movie);
        if (possibleId.isPresent()) {
            return possibleId.get();
        } else {
            String id = java.util.UUID.randomUUID().toString();
            storage.put(id, movie);
            return id;
        }
    }

    public Optional<Movie> getMovie(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    private Optional<String> checkIfMovieExists(Movie movie) {
        return storage
                .entrySet()
                .stream()
                .filter(m -> m.getValue().getTitle().equals(movie.getTitle())
                && m.getValue().getYear() == movie.getYear())
                .map(Map.Entry::getKey)
                .findFirst();
    }
}