package ru.practicum.moviehub.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.movies.CreateMovieAction;
import ru.practicum.moviehub.http.handlers.movies.GetAllMoviesAction;
import ru.practicum.moviehub.http.handlers.movies.GetMovieByIdAction;
import ru.practicum.moviehub.http.handlers.movies.MovieAction;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class MoviesHandler extends BaseHttpHandler {
    private MoviesStore store;
    private final CreateMovieAction createMovieAction;
    private final GetMovieByIdAction getMovieByIdAction;
    private final GetAllMoviesAction getAllMoviesAction;

    public MoviesHandler(MoviesStore store) {
        this.store = store;
        this.createMovieAction = new CreateMovieAction(store, responder);
        this.getMovieByIdAction = new GetMovieByIdAction(store, responder);
        this.getAllMoviesAction = new GetAllMoviesAction(store, responder);
    }

    @Override
    protected void handlePostRequest(HttpExchange exchange) throws IOException {
        if (!validateContentType(exchange)) {
            return;
        }
        createMovieAction.handle(exchange);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        Optional<String> movieId = getMovieIdFromRequest(exchange);
        if (movieId.isPresent()) {
            getMovieByIdAction.handle(exchange);
        } else {
            getAllMoviesAction.handle(exchange);
        }
    }

    private Optional<String> getMovieIdFromRequest(HttpExchange exchange) {
        String[] pathElements = exchange.getRequestURI().getPath().split("/");
        return pathElements.length == 3 ? Optional.of(pathElements[2]) : Optional.empty();
    }
}
