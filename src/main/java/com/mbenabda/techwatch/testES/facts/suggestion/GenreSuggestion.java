package com.mbenabda.techwatch.testES.facts.suggestion;

import com.mbenabda.techwatch.testES.domain.Genre;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class GenreSuggestion {
    private final Genre genre;

    public GenreSuggestion(Genre genre) {
        Validate.notNull(genre);
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreSuggestion that = (GenreSuggestion) o;
        return Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre);
    }
}
