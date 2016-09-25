package com.mbenabda.techwatch.testES.facts.answers;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class LikesGenre {
    private final Long genreId;

    public LikesGenre(Long genreId) {
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
        LikesGenre that = (LikesGenre) o;
        return Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId);
    }
}
