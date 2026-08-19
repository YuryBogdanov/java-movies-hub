package ru.practicum.moviehub.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.movies.*;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.Optional;

public class MoviesHandler extends BaseHttpHandler {
    private MoviesStore store;
    private final CreateMovieAction createMovieAction;
    private final GetMovieByIdAction getMovieByIdAction;
    private final GetAllMoviesAction getAllMoviesAction;
    private final FilterMoviesByYearAction filterMoviesByYearAction;

    public MoviesHandler(MoviesStore store) {
        this.store = store;
        this.createMovieAction = new CreateMovieAction(store, responder);
        this.getMovieByIdAction = new GetMovieByIdAction(store, responder);
        this.getAllMoviesAction = new GetAllMoviesAction(store, responder);
        this.filterMoviesByYearAction = new FilterMoviesByYearAction(store, responder);
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
        } else if (hasFilteringParameter(exchange)) {
            filterMoviesByYearAction.handle(exchange);
        } else {
            getAllMoviesAction.handle(exchange);
        }
    }

    private Optional<String> getMovieIdFromRequest(HttpExchange exchange) {
        String[] pathElements = exchange.getRequestURI().getPath().split("/");
        return pathElements.length == 3 ? Optional.of(pathElements[2]) : Optional.empty();
    }

    private boolean hasFilteringParameter(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        String[] queryComponents = query.split("=");
        return queryComponents.length == 2 && queryComponents[0].equals("year");
    }
}
