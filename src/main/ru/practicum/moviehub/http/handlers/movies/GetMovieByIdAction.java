package ru.practicum.moviehub.http.handlers.movies;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.http.handlers.HttpResponder;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;

public class GetMovieByIdAction implements MovieAction {
    private final MoviesStore store;
    private final HttpResponder responder;

    public GetMovieByIdAction(MoviesStore store, HttpResponder responder) {
        this.store = store;
        this.responder = responder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] params = exchange.getRequestURI().getPath().split("/");
        for (String p : params) {
            System.out.println(p);
        }

    }
}
