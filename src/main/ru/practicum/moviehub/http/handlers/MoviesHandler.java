package ru.practicum.moviehub.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.movies.CreateMovieAction;
import ru.practicum.moviehub.http.handlers.movies.GetMovieByIdAction;
import ru.practicum.moviehub.http.handlers.movies.MovieAction;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.Map;

public class MoviesHandler extends BaseHttpHandler {
    private MoviesStore store;
    private final CreateMovieAction createMovieAction;
    private final GetMovieByIdAction getMovieByIdAction;

    public MoviesHandler(MoviesStore store) {
        this.store = store;
        this.createMovieAction = new CreateMovieAction(store, responder);
        this.getMovieByIdAction = new GetMovieByIdAction(store, responder);
    }

    @Override
    protected void handlePostRequest(HttpExchange exchange) throws IOException {
        if (!validateContentType(exchange)) {
            return;
        }
        createMovieAction.handle(exchange);
    }
}
