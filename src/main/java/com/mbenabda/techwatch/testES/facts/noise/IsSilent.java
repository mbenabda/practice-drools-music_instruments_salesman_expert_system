package com.mbenabda.techwatch.testES.facts.noise;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class IsSilent {
    private final Long instrumentId;

    public IsSilent(final Long instrumentId) {
        Validate.notNull(instrumentId);
        this.instrumentId = instrumentId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsSilent isSilent = (IsSilent) o;
        return Objects.equals(instrumentId, isSilent.instrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentId);
    }
}
