package ru.practicum.moviehub.http.handlers.movies;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.HttpResponder;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GetMovieByIdAction implements MovieAction {
    private final MoviesStore store;
    private final HttpResponder responder;

    public GetMovieByIdAction(MoviesStore store, HttpResponder responder) {
        this.store = store;
        this.responder = responder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] pathElements = exchange.getRequestURI().getPath().split("/");
        if (pathElements.length != 3) {
            responder.sendError(exchange, 400, "Bad request", List.of("Incorrect request"));
        }
        try {
            int movieId = Integer.parseInt(pathElements[2]);
            Optional<Movie> movie = store.getMovie(movieId);
            if (movie.isPresent()) {
                Movie movieToReturn = movie.get();
                responder.sendSuccess(exchange, 200, movieToReturn);
            } else {
                responder.sendError(exchange, 404, "Not found");
            }
        } catch (NumberFormatException e) {
            responder.sendError(exchange, 400, "Bad request", List.of("ID must be a number"));
        }
    }
}
