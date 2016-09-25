package com.mbenabda.techwatch.testES.facts.instruments;

import java.util.Objects;

public class CharacteristicInstrument {
    private final Long genreId;
    private final Long instrumentId;

    public CharacteristicInstrument(Long instrumentId, Long genreId) {
        this.instrumentId = instrumentId;
        this.genreId = genreId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacteristicInstrument that = (CharacteristicInstrument) o;
        return Objects.equals(genreId, that.genreId) &&
            Objects.equals(instrumentId, that.instrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, instrumentId);
    }
}
