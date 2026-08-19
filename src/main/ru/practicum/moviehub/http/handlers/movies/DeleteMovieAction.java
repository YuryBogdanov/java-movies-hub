package ru.practicum.moviehub.http.handlers.movies;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.HttpResponder;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DeleteMovieAction implements MovieAction {
    private final MoviesStore store;
    private final HttpResponder responder;

    public DeleteMovieAction(MoviesStore store, HttpResponder responder) {
        this.store = store;
        this.responder = responder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] pathElements = exchange.getRequestURI().getPath().split("/");
        String movieId;
        if (pathElements.length == 3) {
            movieId = pathElements[2];
            if (store.deleteMovieWithId(movieId)) {
                responder.sendSuccess(exchange, 200, "OK");
            } else {
                // TODO: поправить ошибку
                responder.sendError(exchange, 500, "Wrong", List.of());
            }
        } else {
            // TODO: поправить ошибку
            responder.sendError(exchange, 500, "Wrong", List.of());
        }
    }
}
