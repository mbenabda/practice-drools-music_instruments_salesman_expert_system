package com.mbenabda.techwatch.testES.facts.suggestion;

import com.mbenabda.techwatch.testES.domain.Genre;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class GenreSuggestion {
    private final Long genreId;

    public GenreSuggestion(Long genreId) {
        Validate.notNull(genreId);
        this.genreId = genreId;
    }

    public Long getGenreId() {
        return genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreSuggestion that = (GenreSuggestion) o;
        return Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId);
    }
}
