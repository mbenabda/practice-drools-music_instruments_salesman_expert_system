package com.mbenabda.techwatch.testES.facts.budget;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class CanAfford {
    private final Long instrumentId;

    public CanAfford(final Long instrumentId) {
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
        CanAfford that = (CanAfford) o;
        return Objects.equals(instrumentId, that.instrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentId);
    }
}
