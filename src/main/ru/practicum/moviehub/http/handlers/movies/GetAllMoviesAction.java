package ru.practicum.moviehub.http.handlers.movies;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.HttpResponder;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.List;

public class GetAllMoviesAction implements MovieAction {
    private final MoviesStore store;
    private final HttpResponder responder;

    public GetAllMoviesAction(MoviesStore store, HttpResponder responder) {
        this.store = store;
        this.responder = responder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Movie> allMovies = store.getAllMovies();
        responder.sendSuccess(exchange, 200, allMovies);
    }
}
