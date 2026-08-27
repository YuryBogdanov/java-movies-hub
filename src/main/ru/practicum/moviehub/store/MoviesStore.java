package ru.practicum.moviehub.store;

import ru.practicum.moviehub.model.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MoviesStore {
    private HashMap<Integer, Movie> storage = new HashMap<>();

    public Integer storeMovie(Movie movie) {
        Optional<Integer> possibleId = checkIfMovieExists(movie);
        if (possibleId.isPresent()) {
            return possibleId.get();
        } else {
//            String id = java.util.UUID.randomUUID().toString();
            int id = storage.size() + 1;
            storage.put(id, movie);
            return id;
        }
    }

    public Optional<Movie> getMovie(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Movie> getAllMovies() {
        return storage
                .values()
                .stream()
                .sorted()
                .toList();
    }

    public List<Movie> getAllMoviesWithYear(int year) {
        return storage
                .values()
                .stream()
                .filter(m -> m.getYear() == year)
                .toList();
    }

    public boolean deleteMovieWithId(Integer id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public void deleteAllMovies() {
        storage.clear();
    }

    private Optional<Integer> checkIfMovieExists(Movie movie) {
        return storage
                .entrySet()
                .stream()
                .filter(m -> m.getValue().getTitle().equals(movie.getTitle())
                && m.getValue().getYear() == movie.getYear())
                .map(Map.Entry::getKey)
                .findFirst();
    }
}