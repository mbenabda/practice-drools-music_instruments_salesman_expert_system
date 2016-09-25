package com.mbenabda.techwatch.testES.facts.weight;

import java.util.Objects;

public class IsTooHeavy {
    private Long instrumentId;

    public IsTooHeavy(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsTooHeavy that = (IsTooHeavy) o;
        return Objects.equals(instrumentId, that.instrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentId);
    }
}
