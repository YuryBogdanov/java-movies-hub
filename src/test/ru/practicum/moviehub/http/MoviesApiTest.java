package ru.practicum.moviehub.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.api.ErrorResponse;
import ru.practicum.moviehub.api.requests.CreateMovieRequest;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoviesApiTest {
    private static final String BASE = "http://localhost:8080";
    private static MoviesServer moviesServer;
    private static HttpClient client;
    private static MoviesStore store;

    @BeforeAll
    static void beforeAll() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        store = new MoviesStore();
        moviesServer = new MoviesServer(store, 8080);
        moviesServer.start();
    }

    @BeforeEach
    void beforeEach() {
        store.deleteAllMovies();
    }

    @AfterAll
    static void afterAll() {
        moviesServer.stop();
    }

    @Test
    void getMovies_whenEmpty_returnsEmptyArray() throws Exception {
        // given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .GET()
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        assertEquals(200, response.statusCode(), "GET /movies должен вернуть 200");
        assertEquals("[]", response.body(), "GET /movies должен вернуть пустой массив");
    }

    @Test
    void getMovies_whenHasMovies_returnsNonEmptyArray() throws Exception {
        // given
        Movie movie1 = new Movie("Devil wears Prada", 2002);
        Movie movie2 = new Movie("Devil wears Prada 2", 2026);
        store.storeMovie(movie1);
        store.storeMovie(movie2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .GET()
                .build();
        Gson gson = new Gson();
        String resultJson = gson.toJson(List.of(movie1, movie2));

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        assertEquals(200, response.statusCode(), "GET /movies должен вернуть 200");
        assertEquals(resultJson, response.body(), "GET /movies должен вернуть не пустой массив");
    }

    @Test
    void postMovies_createsMovie_whenDataIsCorrect() throws Exception {
        // Given
        CreateMovieRequest requestBody = new CreateMovieRequest("Some like it hot", 1959);
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // when
        int moviesCountBeforeTest = store.getAllMovies().size();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        int moviesCountAfterCreation = store.getAllMovies().size();

        // then
        Movie movie = store.getAllMovies().getFirst();
        assertEquals(0, moviesCountBeforeTest, "До создания фильма хранилище должно быть пустым");
        assertEquals(1, moviesCountAfterCreation, "После создания в хранилище один фильм");
        assertEquals("Some like it hot", movie.getTitle());
        assertEquals(1959, movie.getYear());
    }

    @Test
    void postMovies_returnsError_ifTitleIsAbsent() throws Exception {
        // Given
        CreateMovieRequest requestBody = new CreateMovieRequest(null, 1959);
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        ErrorResponse resp = gson.fromJson(response.body(), ErrorResponse.class);
        assertEquals("Validation error. See details.", resp.getError());
        assertEquals("The title should be up to 100 characters", resp.getDetails().getFirst());
    }

    @Test
    void postMovies_returnsError_ifTitleIsTooLong() throws Exception {
        // Given
        String title = "Some very very very very very very very very very very very very very very very very very very very very very long title";
        CreateMovieRequest requestBody = new CreateMovieRequest(title, 1959);
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        ErrorResponse resp = gson.fromJson(response.body(), ErrorResponse.class);
        assertEquals("Validation error. See details.", resp.getError());
        assertEquals("The title should be up to 100 characters", resp.getDetails().getFirst());
    }

    @Test
    void postMovies_returnsError_ifYearIsTooFarInPast() throws Exception {
        // Given
        String title = "Some like it hot";
        CreateMovieRequest requestBody = new CreateMovieRequest(title, 1859);
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        ErrorResponse resp = gson.fromJson(response.body(), ErrorResponse.class);
        assertEquals("Validation error. See details.", resp.getError());
        assertEquals("The year should be between 1888 and current year", resp.getDetails().getFirst());
    }

    @Test
    void postMovies_returnsError_ifYearIsTooFarInFuture() throws Exception {
        // Given
        String title = "Some like it hot";
        CreateMovieRequest requestBody = new CreateMovieRequest(title, 2030);
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        ErrorResponse resp = gson.fromJson(response.body(), ErrorResponse.class);
        assertEquals("Validation error. See details.", resp.getError());
        assertEquals("The year should be between 1888 and current year", resp.getDetails().getFirst());
    }

    @Test
    void postMovies_returnsError_ifHeadersAreIncorrect() throws Exception {
        // Given
        CreateMovieRequest requestBody = new CreateMovieRequest("Some like it hot", 1859);
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        assertEquals(415, response.statusCode());
    }

    @Test
    void postMovies_returnsError_ifJsonIsInvalid() throws Exception {
        // Given
        String json = "{title:\"Some like it hot\", year: 1959}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        assertEquals(415, response.statusCode());
    }

    @Test
    void getMoviesById_returnsMovie_ifItIsPresent() throws Exception {
        // given
        Movie movie = new Movie("Some like it hot", 1959);
        int movieId = store.storeMovie(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/" + movieId))
                .header("Content-Type", "application/json; charset=UTF-8")
                .GET()
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Gson gson = new Gson();
        Movie responseMovie = gson.fromJson(response.body(), Movie.class);

        // then
        assertEquals(movie, responseMovie);
    }

    @Test
    void getMoviesById_returnsError_ifItIsNotPresent() throws Exception {
        // given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1234"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .GET()
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        assertEquals(404, response.statusCode());
    }

    @Test
    void getMoviesById_returnsError_ifIdIsNotNumber() throws Exception {
        // given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/some_movie"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .GET()
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // then
        assertEquals(400, response.statusCode());
    }
}