package ru.practicum.moviehub.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.movies.CreateMovieAction;
import ru.practicum.moviehub.http.handlers.movies.MovieAction;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.Map;

public class MoviesHandler extends BaseHttpHandler {
    private MoviesStore store;
    private final Map<String, MovieAction> actions;

    public MoviesHandler(MoviesStore store) {
        this.store = store;
        actions = Map.of(
                "POST", new CreateMovieAction(store, responder)
        );
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateContentType(exchange)) {
            return;
        }

        String method = exchange.getRequestMethod();

        MovieAction action = actions.get(method);
        if (action == null) {
            exchange.sendResponseHeaders(405, -1);
        }

        action.handle(exchange);
    }
}
