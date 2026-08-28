package ru.practicum.moviehub.http.handlers.movies;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CreateMovieRequestValidationResult {
    private final String titleValidationResult;
    private final String yearValidationResult;

    CreateMovieRequestValidationResult(String titleValidationResult, String yearValidationResult) {
        this.titleValidationResult = titleValidationResult;
        this.yearValidationResult = yearValidationResult;
    }

    String getTitleValidationResult() {
        return titleValidationResult;
    }

    String getYearValidationResult() {
        return yearValidationResult;
    }

    List<String> getValidationMessages() {
        return Stream.of(getTitleValidationResult(), getYearValidationResult())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
