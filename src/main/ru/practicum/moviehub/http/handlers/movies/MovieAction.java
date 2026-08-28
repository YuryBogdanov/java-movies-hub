package ru.practicum.moviehub.http.handlers.movies;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface MovieAction {
    void handle(HttpExchange exchange) throws IOException;
}
