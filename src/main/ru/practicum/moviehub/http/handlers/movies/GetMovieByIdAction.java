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
        String movieId;
        if (pathElements.length == 3) {
            movieId = pathElements[2];
            Optional<Movie> movie = store.getMovie(movieId);
            if (movie.isPresent()) {
                Movie movieToReturn = movie.get();
                responder.sendSuccess(exchange, 200, movieToReturn);
            } else {
                responder.sendError(exchange, 500, "Wrong", List.of());
            }
        } else {
            // TODO: поправить ошибку
            responder.sendError(exchange, 500, "Wrong", List.of());
        }
    }
}
