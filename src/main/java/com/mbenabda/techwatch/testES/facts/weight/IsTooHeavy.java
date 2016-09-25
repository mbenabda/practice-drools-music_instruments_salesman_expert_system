package com.mbenabda.techwatch.testES.facts.weight;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class IsTooHeavy {
    private final String instrumentName;

    public IsTooHeavy(final String instrumentName) {
        Validate.notBlank(instrumentName);
        this.instrumentName = instrumentName;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsTooHeavy that = (IsTooHeavy) o;
        return Objects.equals(instrumentName, that.instrumentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentName);
    }
}
