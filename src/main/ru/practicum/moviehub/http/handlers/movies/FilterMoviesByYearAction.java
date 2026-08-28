package ru.practicum.moviehub.http.handlers.movies;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.HttpResponder;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.List;

public class FilterMoviesByYearAction implements MovieAction {
    private final MoviesStore store;
    private final HttpResponder responder;

    private final int minYear = 1888;

    public FilterMoviesByYearAction(MoviesStore store, HttpResponder responder) {
        this.store = store;
        this.responder = responder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String[] queryComponents = query.split("=");
        try {
            int year = Integer.parseInt(queryComponents[1]);
            if (validateYear(year)) {
                List<Movie> movies = store.getAllMoviesWithYear(year);
                responder.sendSuccess(exchange, 200, movies);
            } else {
                responder.sendError(
                        exchange,
                        400,
                        "Something has gone wrong",
                        List.of("Year must be from 1888 until current year + 1")
                );
            }
        } catch (NumberFormatException e) {
            responder.sendError(
                    exchange,
                    400,
                    "Something has gone wrong",
                    List.of("A year must be a number")
            );
        }

    }

    private boolean validateYear(int year) {
        return minYear <= year && year <= java.time.LocalDate.now().getYear() + 1;
    }
}
